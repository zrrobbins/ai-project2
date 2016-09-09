# ai-project1
Project 1 for CS4341 (AI)

### Description
This project contains implementations of both Greedy (Best-First) and Iterative Deepening Searches, for Assignment 1 of CS4341. Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspé.

### Running the Searches
This README assumes you are using a UNIX style shell, such as bash, and have the most recent JDK installed. First, compile the Java code:
```bash
javac Main.java
````

Now, you may run the program like so:
```bash
java Main [input_file]
```

An input_file is a .txt file that must abide by the following format:
```
SEARCH_TYPE
START_VALUE
TARGET_VALUE
TIME_LIMIT
OPERATION 1
OPERATION 2
...
OPERATION N
```

You may refer to the included tests as reference for formatting.


### Tests

**test1.txt:**
- *Description:* In this test, both searches complete, but greedy outperforms iterative.
- GREEDY: Finds optimal solution (6 steps) with only 6 nodes expanded, with a maximum search depth of 6.
- ITERATIVE: Also finds an optimal solution (6 steps), but needs to expand 120 nodes, with a maximum search depth of 6.

**test2.txt:**
- *Description:* This test has not solution, and therefore neither search will find one.
- GREEDY: Finds no solution, but exits as expected after time runs out.
- ITERATIVE: Finds no solution, but exits as expected after time runs out.

**test3.txt:**
- *Description:* This is the example provided in the assignment. While both searches find a solution, iterative finds a better one than greedy.
- GREEDY: Finds a solution (5 steps) with 5 nodes expanded, with a maximum search depth of 5.
- ITERATIVE: Finds optimal solution (3 steps) with 38 nodes expanded, with a maximum search depth of 3.

**test4.txt:**
- *Description:* This test starts out in the goal state.
- GREEDY: Search immediately exits with no steps taken and no nodes expanded, with an error of 0 (goal was found successfully).
- ITERATIVE: Search immediately exits with no steps taken and no nodes expanded, with an error of 0 (goal was found successfully).

**test5.txt:**
- *Description:* With a very high branching factor and minimum solution depth of 1000, greedy is able to find a solution, while iterative runs out of time.
- GREEDY: Finds the optimal solution of 1000 steps, with 1000 nodes expanded, and a maximum search depth of 1000.
- ITERATIVE: Runs out of time and exits, nowhere near finding a solution. Number of nodes expanded is extremely high.

### Analysis
The average effective branching factor was 1.18 for Greedy Search and 3.26 for Iterative Deepening Search. The branching factor is higher for IDS because it expands all nodes up to the maximum depth, whereas Greedy Search only expands the nodes with the lowest heuristic value.

In general, the number of steps taken and nodes expanded by Greedy Search was lower than those expanded in IDS. However, in certain examples, such as Example 3, IDS was more successful in finding the true optimal solution, and Greedy Search finds a quick but inefficient solution. Based on these results, it would be more optimal to use Greedy Search when you have harder memory and time constraints, and use IDS otherwise.
