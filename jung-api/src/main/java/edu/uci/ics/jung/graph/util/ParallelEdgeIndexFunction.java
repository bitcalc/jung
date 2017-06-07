/*
 * Created on Sep 24, 2005
 *
 * Copyright (c) 2005, The JUNG Authors 
 *
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see either
 * "license.txt" or
 * https://github.com/jrtom/jung/blob/master/LICENSE for a description.
 */
package edu.uci.ics.jung.graph.util;

import java.util.HashMap;
import java.util.Map;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.Network;

/**
 * A class which creates and maintains indices for parallel edges.
 * Parallel edges are defined here to be the collection of edges 
 * that are returned by <code>graph.edgesConnecting(v, w)</code> for some 
 * <code>v</code> and <code>w</code>.
 * 
 * <p>At this time, users are responsible for resetting the indices 
 * (by calling <code>reset()</code>) if changes to the
 * graph make it appropriate.
 * 
 * @author Joshua O'Madadhain
 * @author Tom Nelson
 *
 */
public class ParallelEdgeIndexFunction<V, E> implements EdgeIndexFunction<E>
{
    protected Map<E, Integer> edge_index = new HashMap<E, Integer>();
    protected Network<V, E> graph;

    /**
     * @param graph the graph for which this index function is defined
     */
    public ParallelEdgeIndexFunction(Network<V, E> graph) {
    	this.graph = graph;
    }
    
    public int getIndex(E edge)
    {
        Integer index = edge_index.get(edge);
        if (index == null) {
        	EndpointPair<V> endpoints = graph.incidentNodes(edge);
        	V u = endpoints.nodeU();
        	V v = endpoints.nodeV();
        	int count = 0;
        	for (E connectingEdge : graph.edgesConnecting(u, v)) {
        		if (!connectingEdge.equals(edge)) {
        			edge_index.put(connectingEdge, count++);
        		}
        	}
    		edge_index.put(edge, count);
    		return count;
        }
        return index.intValue();
    }
    
    public void reset(E edge) {
    	edge_index.remove(edge);
    }
    
    public void reset()
    {
        edge_index.clear();
    }
}
