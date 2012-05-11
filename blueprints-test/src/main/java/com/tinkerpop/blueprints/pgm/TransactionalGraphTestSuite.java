package com.tinkerpop.blueprints.pgm;

import com.tinkerpop.blueprints.pgm.impls.GraphTest;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class TransactionalGraphTestSuite extends TestSuite {

    public TransactionalGraphTestSuite() {
    }

    public TransactionalGraphTestSuite(final GraphTest graphTest) {
        super(graphTest);
    }

    public void testTrue() {
        assertTrue(true);
    }


    /* public void testTransactionsForVertices() {
        TransactionalGraph graph = (TransactionalGraph) graphTest.generateGraph();
        if (graph.getFeatures().supportsVertexIteration) {
            graph.addVertex(null);
            this.stopWatch();
            graph.startTransaction();
            try {
                graph.addVertex(null);
                assertTrue(true);
            } catch (Exception e) {
                System.out.println(e);
                assertTrue(false);
            }
            assertEquals(count(graph.getVertices()), 2);
            graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE);
            printPerformance(graph.toString(), 1, "vertex not added in failed transaction", this.stopWatch());

            assertEquals(count(graph.getVertices()), 1);

            this.stopWatch();
            graph.startTransaction();
            try {
                graph.addVertex(null);
                assertTrue(true);
            } catch (Exception e) {
                assertTrue(false);
            }
            assertEquals(count(graph.getVertices()), 2);
            graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
            printPerformance(graph.toString(), 1, "vertex added in successful transaction", this.stopWatch());
            assertEquals(count(graph.getVertices()), 2);
        }
        graph.shutdown();
    }

    public void testBruteVertexTransactions() {
        TransactionalGraph graph = (TransactionalGraph) graphTest.generateGraph();
        if (graph.getFeatures().supportsVertexIteration) {
            graph.setBufferSize(0);

            this.stopWatch();
            for (int i = 0; i < 100; i++) {
                graph.startTransaction();
                graph.addVertex(null);
                graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
            }
            printPerformance(graph.toString(), 100, "vertices added in 100 successful transactions", this.stopWatch());
            assertEquals(count(graph.getVertices()), 100);

            this.stopWatch();
            for (int i = 0; i < 100; i++) {
                graph.startTransaction();
                graph.addVertex(null);
                graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE);
            }
            printPerformance(graph.toString(), 100, "vertices not added in 100 failed transactions", this.stopWatch());

            assertEquals(count(graph.getVertices()), 100);
            graph.startTransaction();
            assertEquals(count(graph.getVertices()), 100);
            graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE);
            assertEquals(count(graph.getVertices()), 100);

            this.stopWatch();
            graph.startTransaction();
            for (int i = 0; i < 100; i++) {
                graph.addVertex(null);
            }
            assertEquals(count(graph.getVertices()), 200);
            graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
            printPerformance(graph.toString(), 100, "vertices added in 1 successful transactions", this.stopWatch());
            assertEquals(count(graph.getVertices()), 200);

            this.stopWatch();
            graph.startTransaction();
            for (int i = 0; i < 100; i++) {
                graph.addVertex(null);
            }
            assertEquals(count(graph.getVertices()), 300);
            graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE);
            printPerformance(graph.toString(), 100, "vertices not added in 1 failed transactions", this.stopWatch());
            assertEquals(count(graph.getVertices()), 200);
        }
        graph.shutdown();
    }

    public void testTransactionsForEdges() {
        TransactionalGraph graph = (TransactionalGraph) graphTest.generateGraph();
        graph.setBufferSize(1);
        Vertex v = graph.addVertex(null);
        Vertex u = graph.addVertex(null);
        graph.setBufferSize(0);
        assertEquals(graph.getBufferSize(), 0);

        this.stopWatch();
        graph.startTransaction();
        try {
            graph.addEdge(null, v, u, convertId(graph,"test"));
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
        if (graph.getFeatures().supportsVertexIteration)
            assertEquals(count(graph.getVertices()), 2);
        if (graph.getFeatures().supportsEdgeIteration)
            assertEquals(count(graph.getEdges()), 1);
        graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE);
        printPerformance(graph.toString(), 1, "edge not added in failed transaction (w/ iteration)", this.stopWatch());
        if (graph.getFeatures().supportsVertexIteration)
            assertEquals(count(graph.getVertices()), 2);
        if (graph.getFeatures().supportsEdgeIteration)
            assertEquals(count(graph.getEdges()), 0);

        this.stopWatch();
        graph.startTransaction();
        try {
            graph.addEdge(null, u, v, convertId(graph,"test"));
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
        if (graph.getFeatures().supportsVertexIteration)
            assertEquals(count(graph.getVertices()), 2);
        if (graph.getFeatures().supportsEdgeIteration)
            assertEquals(count(graph.getEdges()), 1);
        graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
        printPerformance(graph.toString(), 1, "edge added in successful transaction (w/ iteration)", this.stopWatch());

        if (graph.getFeatures().supportsVertexIteration)
            assertEquals(count(graph.getVertices()), 2);
        if (graph.getFeatures().supportsEdgeIteration)
            assertEquals(count(graph.getEdges()), 1);

        graph.shutdown();
    }

    public void testBruteEdgeTransactions() {
        TransactionalGraph graph = (TransactionalGraph) graphTest.generateGraph();
        graph.setBufferSize(0);
        this.stopWatch();
        for (int i = 0; i < 100; i++) {
            graph.startTransaction();
            Vertex v = graph.addVertex(null);
            Vertex u = graph.addVertex(null);
            graph.addEdge(null, v, u, convertId(graph,"test"));
            graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
        }
        printPerformance(graph.toString(), 100, "edges added in 100 successful transactions (2 vertices added for each edge)", this.stopWatch());
        if (graph.getFeatures().supportsVertexIteration)
            assertEquals(count(graph.getVertices()), 200);
        if (graph.getFeatures().supportsEdgeIteration)
            assertEquals(count(graph.getEdges()), 100);

        this.stopWatch();
        for (int i = 0; i < 100; i++) {
            graph.startTransaction();
            Vertex v = graph.addVertex(null);
            Vertex u = graph.addVertex(null);
            graph.addEdge(null, v, u, convertId(graph,"test"));
            graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE);
        }
        printPerformance(graph.toString(), 100, "edges not added in 100 failed transactions (2 vertices added for each edge)", this.stopWatch());
        if (graph.getFeatures().supportsVertexIteration)
            assertEquals(count(graph.getVertices()), 200);
        if (graph.getFeatures().supportsEdgeIteration)
            assertEquals(count(graph.getEdges()), 100);

        this.stopWatch();
        graph.startTransaction();
        for (int i = 0; i < 100; i++) {
            Vertex v = graph.addVertex(null);
            Vertex u = graph.addVertex(null);
            graph.addEdge(null, v, u, convertId(graph,"test"));
        }
        if (graph.getFeatures().supportsVertexIteration)
            assertEquals(count(graph.getVertices()), 400);
        if (graph.getFeatures().supportsEdgeIteration)
            assertEquals(count(graph.getEdges()), 200);
        graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
        printPerformance(graph.toString(), 100, "edges added in 1 successful transactions (2 vertices added for each edge)", this.stopWatch());
        if (graph.getFeatures().supportsVertexIteration)
            assertEquals(count(graph.getVertices()), 400);
        if (graph.getFeatures().supportsEdgeIteration)
            assertEquals(count(graph.getEdges()), 200);

        this.stopWatch();
        graph.startTransaction();
        for (int i = 0; i < 100; i++) {
            Vertex v = graph.addVertex(null);
            Vertex u = graph.addVertex(null);
            graph.addEdge(null, v, u, convertId(graph,"test"));
        }
        if (graph.getFeatures().supportsVertexIteration)
            assertEquals(count(graph.getVertices()), 600);
        if (graph.getFeatures().supportsEdgeIteration)
            assertEquals(count(graph.getEdges()), 300);
        graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE);
        printPerformance(graph.toString(), 100, "edges not added in 1 failed transactions (2 vertices added for each edge)", this.stopWatch());
        if (graph.getFeatures().supportsVertexIteration)
            assertEquals(count(graph.getVertices()), 400);
        if (graph.getFeatures().supportsEdgeIteration)
            assertEquals(count(graph.getEdges()), 200);

        graph.shutdown();
    }

    public void testPropertyTransactions() {
        TransactionalGraph graph = (TransactionalGraph) graphTest.generateGraph();
        if (!graph.getFeatures().isRDFModel) {
            graph.setBufferSize(0);

            this.stopWatch();
            graph.startTransaction();
            Vertex v = graph.addVertex(null);
            Object id = v.getId();
            v.setProperty("name", "marko");
            graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
            printPerformance(graph.toString(), 1, "vertex added with string property in a successful transaction", this.stopWatch());


            this.stopWatch();
            graph.startTransaction();
            v = graph.getVertex(id);
            assertNotNull(v);
            assertEquals(v.getProperty("name"), "marko");
            v.setProperty("age", 30);
            assertEquals(v.getProperty("age"), 30);
            graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE);
            printPerformance(graph.toString(), 1, "integer property not added in a failed transaction", this.stopWatch());

            this.stopWatch();
            v = graph.getVertex(id);
            assertNotNull(v);
            assertEquals(v.getProperty("name"), "marko");
            assertNull(v.getProperty("age"));
            printPerformance(graph.toString(), 2, "vertex properties checked in a successful transaction", this.stopWatch());

            graph.startTransaction();
            Edge edge = graph.addEdge(null, graph.addVertex(null), graph.addVertex(null), "test");
            if (graph.getFeatures().supportsEdgeIteration)
                assertEquals(count(graph.getEdges()), 1);
            graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
            if (graph.getFeatures().supportsEdgeIteration)
                assertEquals(count(graph.getEdges()), 1);

            this.stopWatch();
            graph.startTransaction();
            edge.setProperty("transaction-1", "success");
            assertEquals(edge.getProperty("transaction-1"), "success");
            graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
            printPerformance(graph.toString(), 1, "edge property added and checked in a successful transaction", this.stopWatch());
            assertEquals(edge.getProperty("transaction-1"), "success");

            this.stopWatch();
            graph.startTransaction();
            edge.setProperty("transaction-2", "failure");
            assertEquals(edge.getProperty("transaction-1"), "success");
            assertEquals(edge.getProperty("transaction-2"), "failure");
            graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE);
            printPerformance(graph.toString(), 1, "edge property added and checked in a failed transaction", this.stopWatch());
            assertEquals(edge.getProperty("transaction-1"), "success");
            assertNull(edge.getProperty("transaction-2"));
        }
        graph.shutdown();
    }

    /*public void testIndexTransactions() {
        TransactionalGraph graph = (TransactionalGraph) graphTest.generateGraph();
        if (graphTest.supportsVertexIndex) {
            graph.setBufferSize(0);

            this.stopWatch();
            graph.startTransaction();
            Vertex v = graph.addVertex(null);
            Object id = v.getId();
            v.setProperty("name", "marko");
            if (graphTest.supportsVertexIteration)
                assertEquals(count(graph.getVertices()), 1);
            v = ((IndexableGraph) graph).getIndex(Index.VERTICES, Vertex.class).get("name", "marko").iterator().next();
            assertEquals(v.getId(), id);
            assertEquals(v.getProperty("name"), "marko");
            graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
            printPerformance(graph.toString(), 1, "vertex added and retrieved from index in a successful transaction", this.stopWatch());


            this.stopWatch();
            if (graphTest.supportsVertexIteration)
                assertEquals(count(graph.getVertices()), 1);
            v = ((IndexableGraph) graph).getIndex(Index.VERTICES, Vertex.class).get("name", "marko").iterator().next();
            assertEquals(v.getId(), id);
            assertEquals(v.getProperty("name"), "marko");
            printPerformance(graph.toString(), 1, "vertex retrieved from index outside successful transaction", this.stopWatch());


            this.stopWatch();
            graph.startTransaction();
            v = graph.addVertex(null);
            v.setProperty("name", "pavel");
            if (graphTest.supportsVertexIteration)
                assertEquals(count(graph.getVertices()), 2);
            v = ((IndexableGraph) graph).getIndex(Index.VERTICES, Vertex.class).get("name", "marko").iterator().next();
            assertEquals(v.getProperty("name"), "marko");
            v = ((IndexableGraph) graph).getIndex(Index.VERTICES, Vertex.class).get("name", "pavel").iterator().next();
            assertEquals(v.getProperty("name"), "pavel");
            graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE);
            printPerformance(graph.toString(), 1, "vertex not added in a failed transaction", this.stopWatch());

            this.stopWatch();
            if (graphTest.supportsVertexIteration)
                assertEquals(count(graph.getVertices()), 1);
            assertEquals(count(((IndexableGraph) graph).getIndex(Index.VERTICES, Vertex.class).get("name", "pavel")), 0);
            printPerformance(graph.toString(), 1, "vertex not retrieved in a successful transaction", this.stopWatch());
            v = ((IndexableGraph) graph).getIndex(Index.VERTICES, Vertex.class).get("name", "marko").iterator().next();
            assertEquals(v.getProperty("name"), "marko");
        }
        graph.shutdown();
    }*/

    // public void testAutomaticIndexKeysRollback()

    /* public void testAutomaticIndexExceptionRollback() {
       if (graphTest.isPersistent && !graphTest.isRDFModel) {
           TransactionalGraph graph = (TransactionalGraph) graphTest.generateGraph();
           Vertex v = graph.addVertex(null);
           Object id = v.getId();
           v.setProperty("count", "1");
           try {
               // This raises an exception in Neo4j
               v.setProperty("count", null);
           } catch (Exception e) {
           }
           v.setProperty("count", "2");
           graph.shutdown();
           graph = (TransactionalGraph) graphTest.generateGraph();
           Vertex reloadedV = graph.getVertex(id);
           assertEquals("2", reloadedV.getProperty("count"));
           graph.shutdown();
       }
   } */

    /*public void testNestedManualTransactions() {
        TransactionalGraph graph = (TransactionalGraph) graphTest.generateGraph();
        graph.setBufferSize(0);
        graph.startTransaction();
        RuntimeException ex = null;
        try {
            graph.startTransaction();
        } catch (RuntimeException e) {
            ex = e;
        }
        assertNotNull(ex);
        assertEquals(TransactionalGraph.NESTED_MESSAGE, ex.getMessage());
        graph.shutdown();
    }

    public void testStopTransactionEmptyBuffer() {
        TransactionalGraph graph = (TransactionalGraph) graphTest.generateGraph();
        graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
        graph.setBufferSize(15);
        graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
        graph.setBufferSize(0);
        graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
        graph.shutdown();
    }

   /* public void testCurrentBufferSizeConsistencyAfterMutatingOperations() {
        TransactionalGraph graph = (TransactionalGraph) graphTest.generateGraph();
        assertEquals(graph.getBufferSize(), 1);
        assertEquals(graph.getMutationCounter(), 0);
        if (graph instanceof IndexableGraph) {
            ((IndexableGraph) graph).dropIndex(Index.VERTICES);
            ((IndexableGraph) graph).dropIndex(Index.EDGES);
            assertEquals(count(((IndexableGraph) graph).getIndices()), 0);
            assertNull(((IndexableGraph) graph).getIndex(Index.VERTICES, Vertex.class));
            assertNull(((IndexableGraph) graph).getIndex(Index.EDGES, Edge.class));
        }

        assertEquals(graph.getMutationCounter(), 0);

        graph.setBufferSize(11);
        assertEquals(graph.getBufferSize(), 11);
        // sail does not increment the buffer for vertex creation as there is no mutation
        Vertex a = graph.addVertex(null);
        Vertex b = graph.addVertex(null);

        int currentBuffer = graph.getMutationCounter();
        assertEquals(graph.getBufferSize(), 11);

        Edge e = graph.addEdge(null, a, b, convertId("test"));
        assertEquals(graph.getMutationCounter(), currentBuffer + 1);
        assertEquals(graph.getBufferSize(), 11);

        e.setProperty("ng", convertId("test2"));
        assertEquals(graph.getMutationCounter(), currentBuffer + 2);
        assertEquals(graph.getBufferSize(), 11);

        graph.removeEdge(e);
        assertEquals(graph.getMutationCounter(), currentBuffer + 3);
        assertEquals(graph.getBufferSize(), 11);

        graph.removeVertex(a);
        if (!graphTest.isRDFModel) {
            assertEquals(graph.getMutationCounter(), currentBuffer + 4);
        }
        assertEquals(graph.getBufferSize(), 11);

        graph.removeVertex(b);
        if (!graphTest.isRDFModel) {
            assertEquals(graph.getMutationCounter(), currentBuffer + 5);
        }
        assertEquals(graph.getBufferSize(), 11);
        graph.shutdown();
    } */

    /*public void testSuccessfulCommitOnGraphClose() {
        if (graphTest.isPersistent) {
            TransactionalGraph graph = (TransactionalGraph) graphTest.generateGraph();
            assertEquals(graph.getBufferSize(), 1);
            Object v1id = graph.addVertex(null).getId();
            graph.setBufferSize(0);
            graph.startTransaction();
            Object v2id = graph.addVertex(null).getId();
            graph.shutdown();

            graph = (TransactionalGraph) graphTest.generateGraph();
            assertEquals(graph.getBufferSize(), 1);
            assertNotNull("Vertex 1 should be persisted", graph.getVertex(v1id));
            assertNotNull("Vertex 2 should be persisted", graph.getVertex(v2id));
            graph.setBufferSize(10);
            assertEquals(graph.getMutationCounter(), 0);
            Vertex v1 = graph.getVertex(v1id);
            Vertex v2 = graph.getVertex(v2id);
            assertEquals(graph.getMutationCounter(), 0);
            v1.setProperty("name", "puppy");
            v2.setProperty("name", "mama");
            assertEquals(graph.getMutationCounter(), 2);
            graph.shutdown();

            graph = (TransactionalGraph) graphTest.generateGraph();
            assertEquals(graph.getBufferSize(), 1);
            assertEquals(graph.getMutationCounter(), 0);
            if (graph instanceof IndexableGraph) {
                assertEquals(((IndexableGraph) graph).getIndex(Index.VERTICES, Vertex.class).count("name", "puppy"), 1);
                assertEquals(((IndexableGraph) graph).getIndex(Index.VERTICES, Vertex.class).get("name", "puppy").iterator().next().getId(), v1id);
                assertEquals(((IndexableGraph) graph).getIndex(Index.VERTICES, Vertex.class).count("name", "mama"), 1);
                assertEquals(((IndexableGraph) graph).getIndex(Index.VERTICES, Vertex.class).get("name", "mama").iterator().next().getId(), v2id);
            }
            assertNotNull("Vertex 1 should be persisted", graph.getVertex(v1id));
            assertNotNull("Vertex 2 should be persisted", graph.getVertex(v2id));
            assertEquals(graph.getVertex(v1id).getProperty("name"), "puppy");
            assertEquals(graph.getVertex(v1id).getPropertyKeys().size(), 1);
            assertEquals(graph.getVertex(v2id).getProperty("name"), "mama");
            assertEquals(graph.getVertex(v2id).getPropertyKeys().size(), 1);
            assertEquals(graph.getMutationCounter(), 0);
            assertEquals(count(graph.getVertices()), 2);
            assertEquals(count(graph.getEdges()), 0);
            assertEquals(graph.getMutationCounter(), 0);
            assertEquals(graph.getBufferSize(), 1);
            graph.shutdown();
        }
    }*/

    /*public void testBulkTransactions() {
        int dummyCounter = 0;
        TransactionalGraph graph = (TransactionalGraph) graphTest.generateGraph();
        graph.setBufferSize(15);
        for (int i = 0; i < 3; i++) {
            graph.addEdge(null, graph.addVertex(null), graph.addVertex(null), convertId(graph,"test"));
            assertTrue(graph.getMutationCounter() > dummyCounter);
            dummyCounter = graph.getMutationCounter();
        }
        assertEquals(BaseTest.count(graph.getEdges()), 3);
        graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE);
        assertEquals(BaseTest.count(graph.getEdges()), 0);

        dummyCounter = 0;
        for (int i = 0; i < 3; i++) {
            graph.addEdge(null, graph.addVertex(null), graph.addVertex(null), convertId(graph,"test"));
            assertTrue(graph.getMutationCounter() > dummyCounter);
            dummyCounter = graph.getMutationCounter();
        }
        assertEquals(BaseTest.count(graph.getEdges()), 3);
        graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE);
        assertEquals(BaseTest.count(graph.getEdges()), 0);

        dummyCounter = 0;
        for (int i = 0; i < 3; i++) {
            graph.addEdge(null, graph.addVertex(null), graph.addVertex(null), convertId(graph,"test"));
            assertTrue(graph.getMutationCounter() > dummyCounter);
            dummyCounter = graph.getMutationCounter();
        }
        assertEquals(BaseTest.count(graph.getEdges()), 3);
        graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
        assertEquals(BaseTest.count(graph.getEdges()), 3);

        graph.shutdown();
    }

    public void testTransactionBufferSize() {
        TransactionalGraph graph = (TransactionalGraph) graphTest.generateGraph();
        if (!graph.getFeatures().isRDFModel) {

            assertEquals(graph.getMutationCounter(), 0);
            assertEquals(graph.getBufferSize(), 1);
            graph.setBufferSize(1000);
            assertEquals(graph.getBufferSize(), 1000);
            assertEquals(graph.getMutationCounter(), 0);

            for (int i = 1; i < 10; i++) {
                graph.addVertex(null);
                assertEquals(graph.getMutationCounter(), i);
                assertEquals(count(graph.getVertices()), i);
            }

            graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE);
            assertEquals(graph.getBufferSize(), 1000);
            assertEquals(graph.getMutationCounter(), 0);
            assertEquals(count(graph.getVertices()), 0);

            for (int i = 1; i < 10; i++) {
                graph.addVertex(null);
                assertEquals(graph.getMutationCounter(), 1);
                graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE);
                assertEquals(count(graph.getVertices()), 0);
                assertEquals(graph.getMutationCounter(), 0);
            }

            assertEquals(graph.getBufferSize(), 1000);
            assertEquals(graph.getMutationCounter(), 0);
            assertEquals(count(graph.getVertices()), 0);

            for (int i = 1; i < 10; i++) {
                graph.addVertex(null);
                assertEquals(graph.getMutationCounter(), 1);
                graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
                assertEquals(count(graph.getVertices()), i);
                assertEquals(graph.getMutationCounter(), 0);
            }

            assertEquals(graph.getBufferSize(), 1000);
            assertEquals(graph.getMutationCounter(), 0);
            assertEquals(count(graph.getVertices()), 9);


        }
        graph.shutdown();
    }

    public void testCompetingThreads() {
        final TransactionalGraph graph = (TransactionalGraph) graphTest.generateGraph();
        int totalThreads = 250;
        final AtomicInteger vertices = new AtomicInteger(0);
        final AtomicInteger edges = new AtomicInteger(0);
        final AtomicInteger completedThreads = new AtomicInteger(0);
        for (int i = 0; i < totalThreads; i++) {
            new Thread() {
                public void run() {
                    try {
                        Random random = new Random();
                        if (random.nextBoolean()) {
                            Vertex a = graph.addVertex(null);
                            Vertex b = graph.addVertex(null);
                            Edge e = graph.addEdge(null, a, b, convertId(graph,"friend"));

                            if (!graph.getFeatures().isRDFModel) {
                                a.setProperty("test", this.getId());
                                b.setProperty("blah", random.nextFloat());
                                e.setProperty("bloop", random.nextInt());
                            }

                            vertices.getAndAdd(2);
                            edges.getAndAdd(1);

                        } else {
                            graph.setBufferSize(0);
                            graph.startTransaction();
                            Vertex a = graph.addVertex(null);
                            Vertex b = graph.addVertex(null);
                            Edge e = graph.addEdge(null, a, b, convertId(graph,"friend"));
                            if (!graph.getFeatures().isRDFModel) {
                                a.setProperty("test", this.getId());
                                b.setProperty("blah", random.nextFloat());
                                e.setProperty("bloop", random.nextInt());
                            }
                            if (random.nextBoolean()) {
                                graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
                                vertices.getAndAdd(2);
                                edges.getAndAdd(1);
                            } else {
                                graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE);
                            }
                        }
                    } catch (Throwable e) {
                        System.out.println(e);
                        assertTrue(false);
                    }
                    completedThreads.getAndAdd(1);
                }
            }.start();
        }

        while (completedThreads.get() < totalThreads) {
        }
        if (!graph.getFeatures().isRDFModel)
            assertEquals(count(graph.getVertices()), vertices.get());
        assertEquals(count(graph.getEdges()), edges.get());
        graph.shutdown();
    }

    // TODO: uncomment this.  It should pass, but doesn't.
    /*public void testTransactionalClear() {
        TransactionalGraph graph = (TransactionalGraph) graphTest.generateGraph();
        graph.setBufferSize(0);
        
        assertEquals(0, count(graph.getEdges()));

        graph.startTransaction();
        Vertex v1 = graph.addVertex(null);
        Vertex v2 = graph.addVertex(null);
        Edge e1 = graph.addEdge(null, v1, v2, "link");
        graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);

        assertEquals(1, count(graph.getEdges()));
        
        graph.startTransaction();
        graph.removeEdge(e1);
        // We have removed an edge and not yet committed the change.
        // However, the edge should appear to be removed until the end of the transaction.
        assertEquals(0, count(graph.getEdges()));
        graph.stopTransaction(TransactionalGraph.Conclusion.FAILURE);
        
        assertEquals(1, count(graph.getEdges()));
        
        graph.startTransaction();
        graph.removeEdge(e1);
        graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
        
        assertEquals(0, count(graph.getEdges()));
    }*/

}
