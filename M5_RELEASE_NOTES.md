# M5 Release Notes – Farm, Garden and Livestock Diaries

## Included
- Bilingual offline diary for field, garden/orchard, animal, expense, labour, input and production activities.
- Add, edit, delete, category filtering and text search.
- Record date, related crop/plant/animal, quantity, unit, amount, notes and optional reminder date.
- Local one-time reminders attempted around 9:00 AM; delivery state is stored to prevent repeats when users reopen the diary. Android WorkManager/battery management can delay delivery. Notification permission is requested only when the user saves a reminder; if notifications are disabled, delivery remains pending until permission is enabled.
- Up to five private photo attachments per entry. Photos are resized, orientation-corrected and recompressed to strip original image metadata.
- CSV export initiated by the user. CSV values are quoted and formula-like text is neutralized to reduce spreadsheet injection risk.
- Full ZIP backup v2 and restore initiated by the user. Backup includes farmer profile, animal profiles, animal-health history, diary records and attached photos; earlier v1 backups remain importable.
- Room migrations v4→v5→v6→v7 preserve existing records and add diary/photo storage plus persisted one-time reminder delivery state without destructive migration.

## Privacy and data safety
- All farm and diary records remain in the app's local database by default.
- Photos are stored in the app's private files directory.
- Android automatic app-data backup remains disabled.
- No background upload, cloud sync, user account or data sale.
- Backup is written only to the location selected by the user.
- ZIP backups are not password-encrypted; they may contain personal/contact and farm records. Store and share them carefully. Export is limited to 100 MB uncompressed, 12 MB per photo, and 599 photo files per archive.
- Restore replaces the current local farmer, animal, animal-health and diary data. The UI warns the user to create a backup first.
- Archive structure, duplicate/unknown entries, photo manifest, record IDs, dates, numeric values and animal-health foreign-key relationships are validated before database replacement; table replacement occurs in a Room transaction. Existing reminder work is cancelled and rebuilt for restored records.
- A backup archive should be kept in a safe personal location. It may contain private farm and contact information.

## Release acceptance still required on a physical device
1. Upgrade a populated M4 installation through v4→v5→v6→v7 and verify farmer, animal and health records remain intact.
2. Attach photos from gallery; verify orientation, readable filenames, edit/remove/delete and app restart.
3. Create a full ZIP backup, restore it on a test installation and verify all data and photos.
4. Try an invalid/corrupt ZIP; verify it is rejected without replacing current records.
5. Test notification permission, scheduled reminders, reboot and battery optimization behavior.
6. Test CSV export, offline use, Hindi/English layouts and long-text/small-screen layout.
7. Verify no farm data or photos are uploaded automatically.

## Explicit limitations
- M5 is a record-keeping tool, not a crop or veterinary diagnosis system.
- Reminders are best-effort local notifications, not guaranteed alarms.
- CSV contains record metadata and photo counts, not image binary data; full ZIP backup includes photos.
- PDF report generation, multi-farm parcel management and cloud synchronization are not part of M5.

## Cloud verification
- Release-hardening unit tests, debug APK build and artifact upload succeeded in [GitHub Actions run 43411964102](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/actions/runs/43411964102). The tested code was merged to main in [PR #4](https://github.com/tulsaparihar-web/Kisan-Ka-Dost/pull/4).
- APK artifact downloaded and inspected. Physical-device acceptance remains outstanding and is not claimed as complete.
