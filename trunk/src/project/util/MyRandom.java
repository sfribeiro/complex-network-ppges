package project.util;

import java.util.Random;

import jmetal.util.IRandomGenerator;

public class MyRandom implements IRandomGenerator {

	private Random r;

	public MyRandom(long seed) {
		r = new Random(seed);
	}

	@Override
	public int nextInt(int upperLimit) {
		return r.nextInt(upperLimit);
	}

	@Override
	public double nextDouble() {
		return r.nextDouble();
	}

}
