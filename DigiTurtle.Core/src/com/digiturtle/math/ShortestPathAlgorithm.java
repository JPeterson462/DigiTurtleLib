package com.digiturtle.math;

import java.util.ArrayList;
import java.util.Arrays;

import org.joml.Vector2d;

import com.digiturtle.core.Logger;

// Djikstra's Algorithm
public class ShortestPathAlgorithm {
	
	private Vector2d[] vertices;
	
	private int[][] edges;
	
	private int[] prev;
	
	public ShortestPathAlgorithm(Vector2d[] points, int[][] edges, int start) {
		this(points, edges);
		prev = computePathsFrom(start);
	}
	
	public ShortestPathAlgorithm(Vector2d[] points, int[][] edges) {
		vertices = points;
		this.edges = edges;
	}
	
	public int getVertexCount() {
		return vertices.length;
	}
	
	public Vector2d getPoint(int index) {
		return vertices[index];
	}
	
	private int[] computePathsFrom(int start) {
		ArrayList<Integer> Q = new ArrayList<>();
		double[] dist = new double[vertices.length];
		int[] prev = new int[vertices.length];
		for (int v = 0; v < vertices.length; v++) {
			dist[v] = Double.POSITIVE_INFINITY;
			prev[v] = -1;
			Q.add(v);
		}
		dist[start] = 0;
		while (!Q.isEmpty()) {
			double minDist = Double.POSITIVE_INFINITY;
			int u = -1;
			for (int i = 0; i < Q.size(); i++) {
				int n = Q.get(i);
				if (dist[n] < minDist) {
					minDist = dist[n];
					u = n;
				}
			}
			Q.remove((Integer) u);
			for (int v : edges[u]) {
				double alt = new Vector2d(vertices[u]).sub(vertices[v]).length() + dist[u];
				if (alt < dist[v]) {
					dist[v] = alt;
					prev[v] = u;
				}
			}
		}
		Logger.debug("computePathsFrom(" + start + ")", Arrays.toString(prev), Arrays.toString(dist));
		return prev;
	}
	
	public int[] getShortestPath(int start, int end) {
		int[] prevToUse = prev;
		if (prevToUse == null) {
			prevToUse = computePathsFrom(start);
		}
		ArrayList<Integer> path = new ArrayList<>();
		int v = end;
		while (v != start) {
			path.add(v);
			int u = prevToUse[v];
			v = u;
		}
		path.add(start);
		int[] pathArray = new int[path.size()];
		for (int i = 0; i < path.size(); i++) {
			pathArray[i] = path.get(i);
		}
		return pathArray;
	}
	
}
