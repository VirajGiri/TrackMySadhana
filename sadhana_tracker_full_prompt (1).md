
# Sadhana Tracker Android App – Full Implementation Prompt (Page Wise)

You are a senior Android engineer. Build a fully offline Android application called **Sadhana Tracker** for mantra jap tracking used by a spiritual practitioner (sadhak).

The application must work fully offline using Room Database (SQLite) and MVVM architecture.

No authentication.
No API calls.
No internet permission.

Technology stack:

Language: Kotlin
Architecture: MVVM
UI: Material 3
Database: Room
Navigation: Jetpack Navigation
Dependency Injection: Optional (Hilt allowed)
Minimum SDK: 26

Theme style:

Spiritual dark theme
Primary color: Saffron (#8D4B1A)
Accent color: Gold (#FFC107)

--------------------------------

APP PAGES

1. Dashboard (Mukhya Sadhana List)
2. Add New Sadhana
3. Daily Jap Entry
4. History Screen
5. Reports & Insights

--------------------------------

PAGE 1 – DASHBOARD

Purpose:
Show all Mukhya Sadhanas.

Rules:

Naman / Awahan / Dhyan mantras belong to a Mukhya Sadhana internally.

Dashboard shows only Mukhya Sadhana cards.

Card shows:

Sadhana Name
Mala Progress (Completed / Target)
Days Progress (Completed / Target)
Progress bar
Completion %

Example:

Ganesh Mukhya Sadhana
26 / 84 Mala • 9 / 15 Days
Progress bar
31% Completed

Entire card clickable.
Click opens Daily Jap Entry screen.

--------------------------------

PAGE 2 – ADD NEW SADHANA

Fields:

Mantra Name

Multiple Naman / Awahan / Dhyan mantras

Daily Mala Jap Target

Total Mala Jap Target

Start Date

End Date auto calculated.

Row Layout:

Row 1
Daily Mala Jap Target | Total Mala Jap Target

Row 2
Start Date | End Date

End date calculation:

daysNeeded = totalTarget / dailyTarget
endDate = startDate + daysNeeded

--------------------------------

PAGE 3 – DAILY JAP ENTRY

Sections:

Sadhana Name

Today Target

Completed Today

Jap Counter

Daily Progress

Overall Progress

Today's Experience comment box

Save Entry button

--------------------------------

PAGE 4 – HISTORY

RecyclerView grouped by date.

Example:

April 24

Naman / Awahan Mala: 5
Mukhya Mala: 13

Edit entry option.

--------------------------------

PAGE 5 – REPORTS

Statistics:

Total Mala Completed
Days Maintained
Missed Days
Longest Streak
Average Mala / Day
Completion Estimate

Graph showing daily mala.

--------------------------------

DATABASE

Table: MukhyaSadhana
id
name
daily_mala_target
total_mala_target
start_date
end_date

Table: SubMantra
id
sadhana_id
type
mantra_text

Table: JapEntry
id
sadhana_id
date
mala_count
experience_note

--------------------------------

END
