# Kisan Ka Dost – Engineering QA Audit (2026-11-19)

**Audited ref:** `feature/m6-ux-plant-search-tools`  
**Audited commit:** `ab5b87ce23c66a90e4cf63af3e9ec9290979ecf0`  
**Audit type:** Static source/configuration review. This is not a physical-device test and does not claim that runtime defects have been reproduced.

## Executive result

The current branch has a broad offline-first foundation and a successful M8 build, but it is **not yet release-validated**. The latest GitHub Actions workflow compiles the app, runs local JVM unit tests and packages a debug APK. It does not run Android instrumentation/UI tests, Room migration tests, device tests, performance benchmarks or model-accuracy validation.

No confirmed production crash or data-corruption bug was established by this static review. The following gaps should be addressed before calling the app field-ready.

## Findings – prioritized

### P0 – Release gate: verify database migrations with real Room databases
- The Room database is currently schema version 7 and defines explicit migrations 1→2→3→4→5→6→7.
- Existing tests cover backup validation and selected business logic, but no Room migration test is present in the test tree.
- **Action:** add Android instrumentation migration tests using populated databases at historical schema versions; verify farmer profile, animals, animal-health records and diary/photo/reminder fields survive upgrades. Test foreign-key cascade behaviour and backup/restore after upgrade.
- **Acceptance:** every supported historical version upgrades without data loss, schema mismatch, crash or orphaned health records.

### P0 – Release gate: physical-device verification of Plant Doctor
- The model is fetched from a pinned external GitHub revision during CI and checked against a fixed Git blob hash. This is a build-time dependency, not a runtime API dependency.
- Static source review cannot validate model output quality, supported-class accuracy, camera/gallery behaviour, image orientation, inference latency, memory or battery use.
- **Action:** test supported and unsupported crops, healthy leaves, low-light/blurred images, multiple leaves and non-leaf images on low-/mid-range phones. Build an expert-labelled validation set and report per-class precision/recall, confusion matrix and unsupported-image false-positive rate.
- **Acceptance:** unsupported/low-confidence images are clearly rejected or qualified; no diagnosis is presented as certain; no pesticide prescription is generated.

### P1 – Important: add UI and integration automation
- Current workflow runs `:app:testDebugUnitTest` and `:app:assembleDebug`; there are no Android UI/instrumentation tests in the repository tree.
- **Action:** add a small smoke suite for app launch, bottom navigation, Hindi/English preference persistence, core form validation, diary save/edit/delete, animal registration, M8 search and offline use.
- **Acceptance:** smoke suite runs on at least one emulator API level in CI; keep physical-device acceptance separate.

### P1 – Important: reminder lifecycle coverage
- Diary and animal reminders use WorkManager and notification permission handling. Unit tests cover reminder-delay calculation, but not the end-to-end lifecycle across permission denial/grant, editing a reminder, app restart, reboot/Doze or notification delivery.
- **Action:** add scheduler/worker integration tests and device tests. Verify rescheduling after notification permission is granted later and that editing/clearing a reminder cancels or replaces the old work.
- **Acceptance:** no stale duplicate reminders; reminders are clearly described as best-effort because Android may defer work.

### P1 – Important: backup/restore field acceptance
- Backup restore is deliberately destructive to the current local dataset after validation, and the UI warns that restore replaces existing local data. The ZIP is not password-protected.
- **Action:** device-test complete backup→restore including farmer profile, animals, health history, diary entries and photos; test corrupt ZIP, oversized files, missing photos, low storage and interrupted restore. Consider optional encrypted backup in a future improvement, with a user-held password/key and recovery warning.
- **Acceptance:** invalid archives do not replace existing records; valid restore is complete; user understands replacement and unencrypted-file risks.

### P1 – Important: documentation drift
- README milestone summaries still describe some delivered modules as placeholders or planned work, while the active branch contains working Diary, Animal Registry, Government Schemes and M8 Knowledge Assistant screens.
- The README describes M8 as Farm Economics/Smart Assistance, while the recently added M8 screen is an offline keyword-based knowledge assistant. These are different scopes.
- **Action:** reconcile README and milestone tracker. Record Knowledge Assistant as an interim/added capability without implying that farm economics calculations have shipped or that M8 economics is complete.
- **Acceptance:** documentation distinguishes implemented, cloud-build-verified, and physical-device-tested features, and names the actual M8 deliverable accurately.

### P2 – Improvement: data validation and localization consistency
- Diary form currently validates non-negative numeric quantity/amount and date format. Broader module-wide boundary validation and localized numeric/unit entry are not covered by an automated end-to-end suite.
- **Action:** centralize validation for numeric values, date ranges and unit conversions; test Hindi digits, decimal separators, whitespace, very large values and malformed inputs. Avoid silently converting invalid values to zero.
- **Acceptance:** invalid values receive field-specific bilingual messages; calculations use documented units and precision.

### P2 – Improvement: observability and performance baselines
- No benchmark or telemetry test target is configured in the current workflow.
- **Action:** add Macrobenchmark/Perfetto runs for cold/warm launch, navigation, local search, diary list with large datasets and Plant Doctor inference. Record p50/p95 latency, frame jank, peak memory and battery under a repeatable device/brightness/network setup.
- **Acceptance:** publish measured baselines; do not describe target numbers as achieved until measured.

### P2 – Improvement: accessibility and field usability
- Static review cannot establish touch-target size, TalkBack semantics, sunlight readability, font scaling or small-screen overflow.
- **Action:** run accessibility checks and physical-device checks at 1.0×/1.3×/1.5× font scale, Hindi and English, bright outdoor light and narrow display widths.
- **Acceptance:** no clipped critical controls/text; key actions have meaningful accessibility labels; touch targets are comfortable for field use.

## Current test/build boundary

The successful workflow at [run 44509661674](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions/runs/44509661674) confirms the current branch compiled, passed the configured JVM unit tests and produced a debug APK. It does **not** prove camera operation, model accuracy, database migration safety on populated devices, notification delivery, battery performance, field connectivity resilience or full end-to-end correctness.

## Recommended next engineering sequence

1. Add Room migration/integration test infrastructure and test supported schema upgrades.
2. Add a minimal Compose UI smoke suite and run it in CI.
3. Execute Plant Doctor and backup/restore acceptance on physical devices.
4. Verify reminder lifecycle and notification permission flows.
5. Reconcile README and milestone tracker with the actual branch.
6. Establish repeatable performance and accessibility baselines.

**Branch protection:** keep this audit and any follow-up changes on the feature branch. Do not merge to `main` without explicit approval.


---

## Release finalization update – 2026-11-20

The earlier audit above is a static review dated 2026-11-19. This addendum records the latest build and the user's subsequent phone test; it does not imply that every deeper test case has been executed.

### Confirmed since the earlier audit
- The current feature branch contains added Room migration test infrastructure and a Compose navigation smoke test in the Android instrumentation source set. The emulator workflow is separate and manually triggered; the successful APK workflow does not run those instrumentation tests.
- Offline Knowledge Assistant content was expanded with Rajasthan-focused agriculture, horticulture and livestock topics, with corresponding JVM search tests.
- GitHub Actions run [44687212246](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions/runs/44687212246) on commit `e977cc13d401f7c682c46f939050c81e602459b6` passed the configured JVM unit tests, Kotlin build, debug APK packaging and artifact upload.
- The user installed/tested the resulting APK on an Android phone and reported that it works. Treat this as a successful general smoke test only.

### Scope decision
- Freeze feature scope for the current release. Do not add live mandi prices, live weather, paid/third-party API dependencies, cloud sync, mandatory accounts, service directory expansion, farm-economics modules or AI/LLM chat.
- Keep the existing offline features and three equal pillars. Work only on confirmed defects, data integrity, usability, localization and release documentation.

### Still not exhaustively verified
- Running the separate emulator instrumentation workflow and reviewing its results.
- Populated historical Room migration paths on real upgrade data.
- Full backup/restore including photos and animal-health history, plus corrupt/oversized archive and interrupted-restore cases.
- Reminder delivery through permission changes, reboot, Doze and battery restrictions.
- Plant Doctor class-by-class accuracy, unsupported-image false positives and performance across representative devices.
- Formal accessibility and performance baselines.

Do not describe these deeper checks as passed unless evidence is recorded. The current APK is user-smoke-tested and CI build/unit-test verified; release readiness still requires a final commit build/artifact check and an explicit decision about whether the remaining deeper QA items are release blockers or accepted limitations.

**Branch protection:** this update is for `feature/m6-ux-plant-search-tools` only. Do not merge to `main` without explicit approval.
