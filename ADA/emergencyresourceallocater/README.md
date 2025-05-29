# Emergency Resource Allocator using TSP Algorithm 🚨🗺️

> Developed by **R. Navyashree**, Department of ISE  
> Under the subject: **Analysis and Design of Algorithms (ADA)**  
> Project Type: **Lab Mini Project**

---

## 💡 Problem Statement

In emergency scenarios like natural disasters, optimizing the route for resource allocation is crucial. This project implements a **Travelling Salesman Problem (TSP)** based solution to determine the most efficient path for reaching multiple affected sites from a base location, based on:
- Urgency level
- Deadline for each site
- Required resources
- Inter-site distances

---

## 🔍 Features

- Takes input of distances between all sites including the base.
- Accepts site-specific urgency, deadline, and resource requirements.
- Calculates the best route to maximize emergency response efficiency.
- Calculates performance score based on urgency & punctuality.
- Highlights late responses with colored terminal output.

---

## 📊 Algorithm

This project uses **backtracking-based TSP** with scoring heuristics:
- **10 × urgency** if reached before deadline.
- **3 × urgency** if reached late.

---

## 🧪 Sample Input
Enter number of sites (including base, max 10): 4<br>
Enter 4x4 distance matrix:<br>
0 10 15 20<br>
10 0 35 25<br>
15 35 0 30<br>
20 25 30 0<br>
Enter details for each site (ID Urgency Deadline ResourceNeed):<br>
A1 5 25 3<br>
B2 7 30 2<br>
C3 4 20 5<br>

---

## 💻 Output Preview


Calculating best emergency route...

Best Emergency Route:
1. Base (Location 0) - Arrival: 0 min
2. A1 (Location 1) - Arrival: 10 min
3. C3 (Location 3) - Arrival: 30 min
4. B2 (Location 2) - Arrival: 60 min
5. Base (Location 0) - Return
Total Time: 95 mins

=== Emergency Response Summary ===
1. A1 (Location 1) - Arrival: 10 min, Deadline: 25 - On-Time
2. C3 (Location 3) - Arrival: 30 min, Deadline: 20 - Late
3. B2 (Location 2) - Arrival: 60 min, Deadline: 30 - Late<br>
Total Sites:3<br>
On-Time: 1, Late: 2<br>
Urgency Total: 16, Resource Needs Total: 10<br>
Final Performance Score: 101<br>
==================================
---
🧰 **Tools Used**
<br>
* Language: C

* Platform: GCC/MinGW (or any standard C compiler)

* IDE: VS Code or Code::Blocks

* Dependencies: None (Standard C Library only)
<br>

---

**📌 Future Enhancements**

<br>

 *  GUI Interface for easier input.

 * Dynamic loading of locations from file.

 * Integration with maps and real-time traffic APIs.



