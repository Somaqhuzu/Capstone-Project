# Biofilm Simulation Project (Redo)

## 📌 Overview

This project is a simulation of bacterial biofilm formation, developed as part of the BSc Computer Science capstone project. The simulation models the growth of biofilm in a multithreaded environment, where each bacterium is represented as a separate thread that interacts with its environment and other bacteria.

> ⚠️ This project is a **redo** submission after an initial failed attempt. It aims to improve on previous shortcomings by focusing on robust multithreaded design, modular structure, and correct visualization using JavaFX.

---

## 🧬 Simulation Concept

- Each **Bacteria** object is a thread that moves and interacts within a 3D space.
- The **Universe** is represented as a JavaFX GUI with a grid layout.
- A **Local** cell represents a discrete spatial region of the environment.
- Bacteria can secrete **EPS** (Extracellular Polymeric Substances) and attach to surfaces under certain conditions.
- Environment sharing and spatial occupation are synchronized to avoid race conditions.

---

## 🚀 How to Run

### **Main Class:**  
```bash
App.java
Arguments:
<initialBacteriaPopulation> – Number of bacteria to initialize.

<dimensionSize> – Size of the universe (assumed cubic, i.e., applies to width, height, and depth).

<gridCellSize> – Size of each cell/grid unit in the visual environment.
```
Credits:
Author: Onele Nkangezi
Supervisor: Michelle Kuttel,PhD, UCT Department of Computer Science
