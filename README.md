# Flow Aggregation Service
## Introduction
The goal of the service is to accept monitoring events, aggregating them and returning these aggregations.
## Use cases

## Tradeoffs
## Current Design
This service exposes 2 APIs: GET /flows?hour=hour and POST /flows. Read API returns list of events aggregated by
subset of flow attributes. Write API accepts list of flows.

Each flow consist of hour, source_app, destination_app, vpc_id, bytes rx and bytes tx. Attributes are divided into 
2 categories, dimensions and metrics. Dimension is the attribute is used to slice and dice multiple types of aggregations
of metrics. In this design, only sum aggregation is supported. Flow attributes hour, source_app, destination_app and vpc_id 
are dimensions and bytes rx and bytes tx are metrics. 

Current implementation is backed by flows db implemented using Roaring bitmap.
In current implementations, only 2 dimensions are created: hour and combination of source_app, destination_app and vpc_id
referred as flow_key. As part of this demo, Read API is only interested in aggregating by flow_key and filtered by hour, 
which is why 2 dimensions are sufficient.

Table 1

| Hour | Source App | Dest App | Vpc Id  | Bytes Rx | Bytes Tx |     
|------|------------|----------|---------|----------|----------|
| 1    | app1       | app2     | vpc1    | 10       | 11       |
| 1    | app1       | app2     | vpc1    | 20       | 21       |     
| 2    | app1       | app2     | vpc1    | 30       | 31       |    
| 2    | app1       | app2     | vpc1    | 40       | 41       |   
| 2    | app1       | app2     | vpc1    | 50       | 51       |       
| 2    | app1       | app3     | vpc1    | 60       | 61       |      
| 2    | app1       | app3     | vpc1    | 70       | 71       |     
| 2    | app1       | app3     | vpc1    | 80       | 81       |    
| 2    | app2       | app4     | vpc1    | 90       | 91       |   
| 2    | app2       | app4     | vpc1    | 100      | 101      |  

Table 2

| 0     | 1     | 2     | 3     | 4     | 5     | 6     | 7     | 8     | 9       |   
|-------|-------|-------|-------|-------|-------|-------|-------|-------|---------|
| 10,11 | 20,21 | 30,31 | 40,41 | 50,51 | 60,61 | 70,71 | 80,81 | 90,91 | 100,101 |

Table 3

| Hour  | Bitmap (of indices)      |   
|-------|--------------------------|
| 1     | [0, 1]                   |
| 2     | [2, 3, 4, 5, 6, 7, 8, 9] |

Table 4

| Flow Key       | Bitmap (of indices |   
|----------------|--------------------|
| app1,app2,vpc1 | [0, 1, 2, 3, 4]    |
| app1,app3,vpc1 | [5, 6, 7]          |
| app2,app4,vpc1 | [8, 9]             |

For example, data in table 1 is represented as data structures in table 2, 3 and 4.

