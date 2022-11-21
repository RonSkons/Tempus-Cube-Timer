# My Personal Project: Tempus
_Tempus_ is a speedcubing timer programmed in Java. It will be used to time Rubik's Cube solves, generate random cube scrambles, track personal records, and display statistics and visualizations that show trends in recorded times.
After a solve is complete, the solve time will be saved so that it persists after the program is terminated. Times will also be able to be manually added and removed.

## Who is this for, and why am I making it?

_Speedcubers_ (or _cubers_ for short) are people who enjoy solving twisty puzzles, like the Rubik's Cube, as quickly as possible. My personal best is just under 9 seconds!

While it is possible to time yourself using a stopwatch (or stopwatch app), it is clunky and does not keep track of the *history* of your solves, which is essential for gauging improvement and progression.

Cube timers are, in essence, souped-up stopwatches which record every time that they measure. They can also contain helpful features such as scramble generation and data visualizations.

There exist several cubing timers, but none are perfect in my opinion. I have been meaning to design my own for a while now, and I believe that I can do so while fulfilling the requirements of the term project.

## User Stories

- As a user, I want to be able to time multiple solves and add them to a list of solves 
- As a user, I want to receive directions on how to scramble my cube in a fair and random manner
- As a user, I want to know my average solve time at any given moment
- As a user, I want to keep track of my fastest solve time
- As a user, I want to be able to delete a solve from a list of solves, or clear the entire list
- As a user, I want to be able to save my list of times when I quit the application
- As a user, I want my previously saved times to be present when I re-open the application
- As a user, I want to export a string representation of my solves so that I can view them elsewhere

# Instructions for Grader
- You can generate the first required event related to adding Solves to a SolveList by either:
  - Starting and stopping the timer by pressing the space key or timer button
  - Clicking the "+" button to manually add a Solve
- You can generate the second required event related to adding Solves to a SolveList by clicking the "Export" button
  - This will present a dialog with a string representation of all Solves in the SolveList
- You can locate my visual component by looking at the beautiful image at the top of the application window
- You can save the state of my application by closing the application and selecting "Yes" when prompted
- You can reload the state of my application by opening the application and selecting "Yes" when prompted

# Phase 4: Task 2
Mon Nov 21 13:24:01 PST 2022
Added a solve to the list: 15.621

Mon Nov 21 13:24:01 PST 2022
Added a solve to the list: 16.494

Mon Nov 21 13:24:01 PST 2022
Added a solve to the list: 16.284

Mon Nov 21 13:24:01 PST 2022
Added a solve to the list: 14.967

Mon Nov 21 13:24:01 PST 2022
Added a solve to the list: 15.821

Mon Nov 21 13:24:01 PST 2022
Added a solve to the list: 16.854

Mon Nov 21 13:24:04 PST 2022
Removed solve number 5 from the list.

Mon Nov 21 13:24:07 PST 2022
Removed solve number 6 from the list.

Mon Nov 21 13:24:17 PST 2022
Added a solve to the list: 9.238

Mon Nov 21 13:24:27 PST 2022
Exported solve list.

# Phase 5: Task 3
I am overall satisfied with the design of this project.
If I had more time, I might want to refactor SolveListJsonReader/Writer into more generic JSON-handling classes, and make Solve and SolveList responsible for (un)serializing themselves.
This would improve cohesion by making sure that my persistence classes are concerned strictly with persistence-related tasks.