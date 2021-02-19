
/* 
 I, <name> (<ID>), assert that the work I submitted is entirely my own.
 I have not received any part from any other student in the class, nor did I give parts of it for use to others.
 I realize that if my work is found to contain code that is not originally my own, a
 formal case will be opened against me with the BGU disciplinary committee.
*/

// Last Update: dd/mm/yy

public class Assignment2 {

	
	/*--------------------------------------------------------
	   Part a: instance representation & Solution verification 
	  -------------------------------------------------------
	 */

	// Task 1
	public static boolean hasFlight(int[][] flights, int i, int j) {
		boolean hasFlight = false;
		for (int indx = 0; (indx < flights[i].length) && (hasFlight == false); indx++) {
			if (flights[i][indx] == j) {
				hasFlight = true;
			}
		}
		return hasFlight;
	}

	// Task 2
	public static boolean isLegalInstance(int[][] flights) {
		boolean isLegal = true;
		if ((flights.length <= 1) || (flights == null)) {
			isLegal = false;
		}
		for (int i = 0; (i < flights.length) && isLegal; i++) {
			if (flights[i] == null) {
				isLegal = false;
			}
		}
		for (int i = 0; (i < flights.length) && isLegal; i++) {
			for (int j = 0; (j < flights[i].length); j++) {
				if ((flights[i][j] < 0) || (flights[i][j] >= flights.length) || (flights[i][j] == i)) {
					isLegal = false;
				}
				int value = flights[i][j];
				int exist = 0;
				for (int indx = 0; indx < flights[value].length; indx++) {
					if (flights[value][indx] == i) {
						exist = 1;
					}
				}
				if (exist != 1) {
					isLegal = false;
				}
			}

		}
		return isLegal;
	}

	// Task 3
	public static boolean isSolution(int[][] flights, int[] tour) {
		if (tour.length != flights.length) {
			throw new IllegalArgumentException("IllegalArgumentException");
		}
		for (int i = 0; i < tour.length; i++) {
			if ((tour[i] < 0) || (tour[i] >= tour.length)) {
				throw new IllegalArgumentException("IllegalArgumentException");
			}
		}
		boolean solution = true;
		int start;
		int destination = 0;
		for (int i = 0; (i < tour.length - 1) && solution; i++) {
			start = tour[i];
			destination = tour[i + 1];
			solution = hasFlight(flights, start, destination);
		}
		solution = solution && hasFlight(flights, destination, tour[0]);
		return solution;
	}
	
	/*------------------------------------------------------
	  Part b: Express the problem as a CNF formula, solve 
	  it with a SAT solver, and decode the solution
	  from the satisfying assignment
	  -----------------------------------------------------
	 */

	// Task 4
	public static int[][] atLeastOne(int[] vars) {
		int[][] formula = new int[1][];
		formula[0] = vars;
		return formula;
	}

	// Task 5
	public static int[][] atMostOne(int[] vars) {
		int[][] formula = new int[0][0];
		int[][] clause = new int[1][2];
		for (int i = 0; i < vars.length - 1; i++) {
			for (int j = i + 1; j < vars.length; j++) {
				clause[0][0] = -1 * vars[i];
				clause[0][1] = -1 * vars[j];
				formula = append(formula, clause);
			}
		}
		return formula;
	}

	// Task 6
	public static int[][] append(int[][] arr1, int[][] arr2) {
		int length = arr1.length + arr2.length;
		int[][] append = new int[length][];
		for (int i = 0; i < arr1.length; i++) {
			append[i] = new int[arr1[i].length];
			for (int j = 0; j < arr1[i].length; j++) {
				append[i][j] = arr1[i][j];
			}
		}
		for (int i = 0; i < arr2.length; i++) {
			append[arr1.length + i] = new int[arr2[i].length];
			for (int j = 0; j < arr2[i].length; j++) {
				append[arr1.length + i][j] = arr2[i][j];
			}
		}
		return append;
	}

	// Task 7
	public static int[][] exactlyOne(int[] vars) {
		int[][] atLeastOne = atLeastOne(vars);
		int[][] atMostOne = atMostOne(vars);
		int[][] exactlyOne = append(atLeastOne, atMostOne);
		return exactlyOne;
	}

	// Task 8
	public static int[][] diff(int[] I1, int[] I2) {
		int[][] diff = new int[I1.length][2];
		for (int i = 0; i < I1.length; i++) {
			diff[i][0] = -1 * I1[i];
			diff[i][1] = -1 * I2[i];
		}
		return diff;
	}

	// Task 9
	public static int[][] createVarsMap(int n) {
		int[][] map = new int[n][n];
		int counter = 1;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				map[i][j] = counter;
				counter++;
			}
		}
		return map;
	}

	// Task 10
	public static int[][] declareInts(int[][] map) {
		int[][] formula = new int[0][0];
		for (int i = 0; i < map.length; i++) {
			formula = append(formula, exactlyOne(map[i]));
		}
		return formula;
	}

	// Task 11
	public static int[][] allDiff(int[][] map) {
		int[][] formula = new int[0][2];
		int[][] diff = new int[0][0];
		for (int i = 0; i < map.length - 1; i++) {
			for (int j = 1; j < map.length; j++) {
				if (i < j) {
					diff = diff(map[i], map[j]);
					formula = append(formula, diff);
				}
			}
		}
		return formula;
	}

	// Task 12
	public static int[][] allStepsAreLegal(int[][] flights, int[][] map) {
		int[][] formula = new int[0][0];
		for (int i = 0; i < flights.length; i++) {
			for (int j = 0; j < flights.length; j++) {
				if (!hasFlight(flights, i, j) & (i != j)) {
					int[][] clause = new int[1][2];
					for (int x = 0; x <= map.length - 2; x++) {
						clause[0][0] = -1 * map[x][i];
						clause[0][1] = -1 * map[x + 1][j];
						formula = append(formula, clause);
					}
					clause[0][0] = -1 * map[map.length - 1][i];
					clause[0][1] = -1 * map[0][j];
					formula = append(formula, clause);
				}
			}
		}
		return formula;
	}

	// Task 13
	public static void encode(int[][] flights, int[][] map) {
		if (!isLegalInstance(flights)) {
			throw new IllegalArgumentException();
		}
		int counter = 1;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				if (map[i][j] != counter) {
					throw new IllegalArgumentException();
				}
				counter++;
			}
		}
		map = append(declareInts(map), append(allStepsAreLegal(flights, map), append(allDiff(map), map)));
		SATSolver.addClauses(map);
	}

	// Task 14
	public static int[] decode(boolean[] assignment, int[][] map) {
		if (assignment.length != (map.length * map.length + 1)) {
			throw new IllegalArgumentException();
		}
		int[] decode = new int[map.length];
		int counter = 0;
		for (int i = 1; i < assignment.length; i++) {
			if (assignment[i]) {
				decode[counter] = (i - 1) % (map.length);
				counter++;
			}
		}
		return decode;
	}

	// Task 15
	public static int[] solve(int[][] flights) {
		int[][] map = createVarsMap(flights.length);
		int[] s = new int[flights.length + 1];
		SATSolver.init((flights.length) * (flights.length));
		encode(flights, map);
		boolean[] sB = SATSolver.getSolution();
		if ((sB != null) && (sB.length != 0)) {
			s = decode(sB, map);
			boolean isSolution = isSolution(flights, s);
			if (!isSolution) {
				throw new IllegalArgumentException();
			}
		} else if (sB == null) {
			throw new IllegalArgumentException();
		} else if (sB.length == 0) {
			s = null;
		}
		return s;
	}

	// Task 16
	public static boolean solve2(int[][] flights, int s, int t) {
		if (s < 0 | s >= flights.length | t < 0 | t >= flights.length | !isLegalInstance(flights)) {
			throw new IllegalArgumentException();
		}
		int[][] map = createVarsMap(flights.length);
		int[] tour = new int[flights.length + 1];
		SATSolver.init((flights.length)*(flights.length));
		encode(flights, map);
		int[] clause1 = new int[]{s + 1};
		SATSolver.addClause(clause1);
		int[] clause2 = new int[]{t+1+map.length*(map.length-1)};
		SATSolver.addClause(clause2);
		boolean[] tourB = SATSolver.getSolution();
		boolean thereAreTwo=false;
		if (tourB== null) {
			throw new IllegalArgumentException();
		}
		else {
			if (tourB.length==0) {
				thereAreTwo = false;
			}
			else {
				tour = decode(tourB, map);
				for (int i = 1; i < tour.length - 1 & (!thereAreTwo); i++) {
					SATSolver.init((flights.length) * (flights.length));
					encode(flights, map);
					int[] clause3 = new int[]{s + 1};
					SATSolver.addClause(clause3);
					int[] clause4 = new int[]{t + 1 + map.length * (map.length - 1)};
					SATSolver.addClause(clause4);
					int[] clause5 = new int[]{-1 * (i + 1 + i * map.length)};
					SATSolver.addClause(clause5);
					boolean[] tourB2 = SATSolver.getSolution();
					if ((tourB2 != null) && (tourB.length != 0)) {
						tour = decode(tourB, map);
						boolean isSolution = isSolution(flights, tour);
						if (isSolution) {
							thereAreTwo = true;
						}
					}
				}
			}
		}
		return thereAreTwo;
	}
}
