import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;
import java.util.HashSet;

public class Day01 {
	public static void main(String[] args) throws IOException {
		// Read in the input, split it into the separate instructions.
		BufferedReader reader = new BufferedReader(
				new FileReader(new File("/Users/vamshiramineni/Documents/workspace/Advent2016/src/day01.txt")));
		String[] instructions = reader.readLine().split(", ");
		reader.close();

		// Keeps track of our position as x and y coordinates.
		int[] pos = new int[] { 0, 0 };
		// Keeps track of the x and y direction we are currently facing.
		// Positive x direction is north, positive y direction is west.
		int[] dir = new int[] { 1, 0 };

		// Keep track of places we have visited for part 2.
		HashSet<String> visited = new HashSet<String>();
		// We start at position (0,0), add this to the set of visited
		// instructions.
		visited.add("(0,0)");

		// Variable for storing the solution for part 2.
		// Initialized at -1 so we know when we have already found the solution.
		int solution2 = -1;

		for (String instruction : instructions) {
			// Rotate left or right.
			dir = instruction.charAt(0) == 'L' ? new int[] { -dir[1], dir[0] } : new int[] { dir[1], -dir[0] };
			// Find the length we have to walk.
			int length = Integer.parseInt(instruction.substring(1));
			if (solution2 == -1) {
				for (int i = 1; i <= length; i++) {
					int[] nextPos = new int[] { pos[0] + i * dir[0], pos[1] + i * dir[1] };
					// String representation of the position so we can compare
					// it to earlier visited locations.
					String nextPosString = "(" + nextPos[0] + "," + nextPos[1] + ")";
					// We visited this location before, we have found the
					// solution to part2!
					if (visited.contains(nextPosString)) {
						solution2 = Math.abs(nextPos[0]) + Math.abs(nextPos[1]);
						break;
					}
					visited.add(nextPosString);
				}
			}
			// Update are position with the current instruction.
			pos = new int[] { pos[0] + length * dir[0], pos[1] + length * dir[1] };
		}

		int solution1 = Math.abs(pos[0]) + Math.abs(pos[1]);
		System.out.println("part1: " + solution1);
		System.out.println("part2: " + solution2);
	}
}
