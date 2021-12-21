# adventofcode2021

## Day 17: Trick Shot
https://adventofcode.com/2021/day/17

Refused to brute force this, even though the problem space is reasonably small.

### Part 1
For a positive initial y velocity, the y value has the same values going down as it does when going up. So there is a y=0 on the way down.
So the fastest that the probe can be going on the next step is the same as the minimum valid Y value (-10 in the example).
So the max velocity on the previous step is one less than this (-9).
And the corresponding starting velocity for this is the same in the other direction (9).
So the max height achievable is the sum of all integers 1..9, or (9 x (9 + 1) / 2) = 45.

So the general solution is `Hmax = (-Ymin - 1) * (-Ymin - 1 + 1) / 2) = Ymin * (Ymin + 1) / 2`

### Part 2
* Work out the minimum and maximum X and Y starting velocities (min X is interesting cos you solve `Xmin = X(X+1)/2` for X).
* Work out for each valid X which ones result in some steps that are on target - record the on target steps for each X.
* Do the same for Y.
* Then work out for each Y, which values of X have the same matching step numbers. Then these are the X, Y pairs that describe all the starting velocities.

There is a wrinkle that if the X velocity drops to 0 and the X value is on target at this step, then the X value will be on target for all future steps (as X is no longer moving).

