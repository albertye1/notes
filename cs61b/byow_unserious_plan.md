# BYOW Plan
* This video is very useful: hide from everyone else (including partner!)
	* https://www.youtube.com/watch?v=fudOO713qYo
* Because I want to flex, let's do multiple methods of world gen
## Standard Generation
* Let's split the map into $m \times n$ zones of equal size. Then try to randomly select corners of rectangles in each zone to build $m \times n$ rooms.
	* But not all of them should have rooms. There's a random probability $p_d \in [0.05, 0.15]$ that we have no room here, in which case we just add a $1 \times 1$ dummy we want to connect.
	* There's also a higher probability $p_c = 0.75$ that we connect adjacent rooms. Then, we pick random locations on each adjacent side and then build a hallway that connects between them. This hallway will bend @ random point
* Snake a random # $s$ cul-de-sacs or something that start @ a random floor tile and go straight w/ a certain chance of turning left or right. End when this line is adj. to a floor tile or the world border.
* If any $1 \times 1$ dummies are not connected to anywhere by the end of this process, delete them from the map
* Room merging: 
```java
// for two adj. rooms i, j
double p = Math.random();
if (p < 0.05) {
	for (int i = Math.min(rooms[i].x1, rooms[j].x1); i < Math.max(rooms[i].x2, rooms[j].x2); i++) {
		for (int j = Math.min(rooms[i].y1, rooms[j].y1); j < Math.max(rooms[i].y2, rooms[j].y2); j++) {
			grid[i][j] = floor;
		}
	}
	// then update the coords of rooms i, j to make further merges easier.
}
```

# Special Generation
Has certain probabilities of happening, and basically makes a $3 \times 3$ grid with set features. Such as rooms only generate on the outside, rooms only generate on the inside, cross formation

I'm not gonna be able to completely copy PMD however, especially not with terrain formation.