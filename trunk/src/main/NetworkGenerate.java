package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class NetworkGenerate {
	
	public static Random r = new Random(-7381);
	
	public static double[][] scaleFree(int numNodes) {
		
		double[][] resp = new double[numNodes][numNodes];

		ArrayList<Integer> restantes = new ArrayList<Integer>();
		for (int i = 0; i < numNodes; i++) {
			restantes.add(i);
		}

		ArrayList<Integer> array = new ArrayList<Integer>();

		int no1 = r.nextInt(numNodes);
		array.add(no1);
		restantes.remove(no1);

		int next;
		while (restantes.size() > 0) {
			next = restantes.remove(r.nextInt(restantes.size()));
			no1 = array.get(r.nextInt(array.size()));
			//System.out.println(next);

			if (next != no1) {
				resp[next][no1] = 1;
				resp[no1][next] = 1;

				array.add(no1);
				array.add(next);
			}
		}
		
		return resp;
	}

	public static double[][] erdosReni(int numNodes, double tax)
	{
		double[][] resp = new double[numNodes][numNodes];
		
		ArrayList<Integer> restantes = new ArrayList<Integer>();
		for (int i = 0; i < numNodes; i++) {
			restantes.add(i);
		}

		for(int i = 0; i< numNodes -1; i++)
		{
			for(int j = i+1; j< numNodes; j++)
			{
				if(r.nextDouble() < tax)
				{
					resp[i][j] = 1;
					resp[j][i] = 1;
				}
			}
		}
		
		return resp;
	}
}
