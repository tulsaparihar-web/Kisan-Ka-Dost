# M4 Release Candidate – Animal Husbandry

## Included
- Bilingual searchable animal-care guide for cattle, buffalo, goats, sheep, camels and poultry.
- Rajasthan breed names and home-tract context refreshed against current ICAR-NBAGR registers.
- Short qualitative breed notes sourced to ICAR-NBAGR, KVK and Government of Rajasthan references.
- Age-/production-stage feeding education, newborn colostrum basics, safe fodder storage/conservation and biosecurity.
- Offline animal registry: add, list, edit and delete individual profiles including species, breed, tag, sex, age/date of birth, purpose and notes.
- Per-animal health-event history: event date, notes and optional next date.
- Next-date reminders only when marked veterinarian-confirmed; WorkManager attempts a local notification around 9:00 AM on the selected date.
- Runtime notification permission request; pending reminders can be re-enqueued after permission is later granted.
- Non-destructive Room migrations from v1 to v2 to v3 to v4; the v3→v4 migration adds the breed field without deleting existing animal records.
- Android automatic app-data backup disabled so farm and animal records remain on-device by default.
- Unit tests for reminder delay calculation.

## Safety and product boundaries
- No disease diagnosis, medicine recommendation, drug dose or universal vaccination/deworming schedule.
- No fixed feed quantities or ration calculator. Individual rations depend on animal profile, production stage, feed analysis and local conditions; consult a veterinarian/animal nutritionist.
- Reminder notifications are inexact Android background work and may be delayed by battery management or suppressed by system notification settings.
- No cloud sync, account requirement, paywall or subscription.

## Release gates still requiring a real phone
1. Upgrade/restore test with a populated M3 database; confirm farmer profile survives Room v1→v2→v3.
2. Add/edit/delete animal records and verify they remain after app restart and offline use.
3. Add/delete a health event; verify a past date, malformed date, next date before event date and unconfirmed next date are rejected.
4. Grant and deny notification permission; test a near-future reminder, app restart and permission re-enable.
5. Verify notification behavior after reboot and under battery optimization/Doze.
6. Check Hindi/English layouts, scrolling and accessibility on a small phone.
7. Re-run M3 Plant Doctor camera/gallery regression checks.

## Review status
Source implementation is complete for the M4 foundation. Cloud unit-test/build and APK packaging must pass before merging. Physical-device release gates above remain open until tested by the app owner.
