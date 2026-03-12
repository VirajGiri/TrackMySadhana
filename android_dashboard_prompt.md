
# Android App Implementation Prompt – Sadhana Dashboard Screen

You are a senior Android developer. Build the first screen (Dashboard) of a fully offline Android application called **Sadhana Tracker**.

## Tech Requirements
- Language: Kotlin
- Architecture: MVVM
- UI: Material 3
- Database: Room (SQLite)
- No authentication
- No internet access
- Dark spiritual theme

## Screen: Dashboard

This screen shows only **Mukhya Sadhanas**.

Rules:
- A user can have multiple Mukhya Sadhanas.
- Each Mukhya Sadhana contains its own Awahan/Naman mantras internally.
- Dashboard should only display Mukhya Sadhana cards.

## Layout Structure

Main Layout:
ConstraintLayout

Top AppBar:
- Title: "Sadhana Tracker"
- Meditation icon on left
- Calendar icon on right

Scrollable Section:
RecyclerView displaying cards.

## Card UI Design

Each card contains:

Sadhana Name
Total Mala Progress (example: 26 / 84 Mala)
Days Progress (example: 9 / 15 Days)
Horizontal Progress Bar
Completion Percentage

Example card:

Ganesh Mukhya Sadhana
26 / 84 Mala • 9 / 15 Days
[Progress Bar]
31% Completed

Entire card must be clickable.

On Click:
Open Daily Entry Screen.

## RecyclerView Item Layout

CardView
    TextView – Sadhana Name
    TextView – Mala Progress
    TextView – Days Progress
    ProgressBar – Horizontal
    TextView – Completion %

## Database Tables

### MukhyaSadhana

id
name
daily_mala_target
total_mala_target
start_date
end_date

### JapEntry

id
sadhana_id
date
mala_count
experience_note

## Progress Calculation

Total Mala Completed:
SUM(jap_entries.mala_count)

Completion Percentage:
(totalCompletedMala / totalTargetMala) * 100

Days Completed:
COUNT(DISTINCT date)

## RecyclerView Binding Logic

for each sadhana:

todayMala = dao.getTodayMala(sadhanaId)
totalMala = dao.getTotalMala(sadhanaId)
progress = totalMala / totalTarget

update progress bar

## UI Theme

Primary Color: #8D4B1A
Accent: #FFC107
Background: Dark saffron texture

Use provided background image.

## Icons Provided

- ic_meditation.svg
- ic_calendar.svg
- ic_progress.svg

## Assets Usage

Place files in:

res/drawable/

background_saffron.png
ic_meditation.svg
ic_calendar.svg
ic_progress.svg

## Expected Output

Dashboard screen with multiple Mukhya Sadhana cards showing progress.

The screen must support unlimited Sadhanas.

Cards must be clickable.
