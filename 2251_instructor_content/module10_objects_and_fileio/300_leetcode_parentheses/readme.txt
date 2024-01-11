
Topics: Looping and counting (and computational thinking)
Exercise for students (Do go over the solution)
https://leetcode.com/problems/valid-parentheses/

The true solution is to use a stack data structure then push open symbols,whether they are (, [, or {, onto the stack. Then when a closing symbol is found, make sure that the closing symbol matches with the opening symbol popped off the stack.
