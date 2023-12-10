# ADT-Project

### Submitted by: Sushant Sinha 40261753

## Table of Contents

- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
    - [Setup](#setup)
    - [Running Simulations](#running-simulations)

## Getting Started

We will only need java to implement all aspects of this project. <b>No</b> external libraries are needed.

### Prerequisites

Users need to install Java before using your project.

### Installation

Step-by-step guide on how to install your project.

```bash
git clone https://github.com/sushant-sinha/ADT-Project.git
cd ADT-Project
```

## Usage

### Setup
First, we need to update the ```GraphGenerator.java``` file to generate the required csv files(if not present already).

```
public static void main(String[] args) {

        // update the parameters here
        int n = 1000;
        double r = 0.2;
        int upperCap = 100;
        String outputFileName = "graph_adjacency_list_"+n+"_"+r+"_"+upperCap+".csv";

        generateGraph(n, r, upperCap, outputFileName);
    }
```

To find the start and end node of a Longest Acyclic path updat the ```LongestAcyclicPathFinder.java``` file:

```
    public static void main(String[] args) {

        // update the inputFile here
        String inputFile = "graph_adjacency_list_1000_0.2_100.csv";
        int n=1000;
        int startNode = (int) ((Math.random() * (n - 1)) + 1);

        System.out.println("Start node is "+startNode);

```  

This will return the start and end node along with the number of hops(path length).

### Running Simulations
For running the simulations, select a version of FordFulkerson:
- FordFulkersonSAP.java
- FordFulkersonDFS.java
- FordFulkersonMaxCap.java
- FordFulkersonRandom.java

Edit the parameters: inputFile, source and sink:

```

    public static void main(String[] args) {
        String inputFile = "graph_adjacency_list_100_0.2_2.csv"; // Specify the inputFile
        int source = 6; // Specify the source node
        int sink = 14;  // Specify the sink node


```
