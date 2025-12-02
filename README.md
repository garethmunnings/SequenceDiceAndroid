# Sequence Dice (Android)

A mobile remake of the board game **Sequence Dice**, built with **Java** and **Android**.  
This project implements the full game logic and rules of Sequence Dice in an interactive Android app.

<p align="center">
   <img width="350" alt="Screenshot_2" src="https://github.com/user-attachments/assets/2170d641-fbe0-45e5-9859-ddf3c1c8323d" />
   <img width="350" alt="Screenshot_1" src="https://github.com/user-attachments/assets/3fec6f48-62df-4194-9edc-6df5dae16fcf" />
</p>

## 📜 Game Rules

Sequence Dice is a variation of the Sequence board game designed for **2–4 players**.  

### 🎯 Objective
Be the first player (or team) to complete:
- **5 tokens in a row** (2 players)
- **6 tokens in a row** (3–4 players)  
Tokens can be aligned **horizontally**, **vertically**, or **diagonally**.

### 🛠 Gameplay
- **Board**: 6x6 grid, cells numbered from **2 to 12**
- **Equipment**: 2 six-sided dice, colored tokens for each player/team
- **Turn Order**: Each player rolls the dice; highest total starts, then play proceeds clockwise

#### 🎲 Dice Outcomes:
1. **Normal Roll** – Place a token on an empty cell matching the dice sum.
2. **Occupied Cells** – Replace an opponent's token if no empty matching cell exists. If all are your own, skip the turn.
3. **Roll a 2 or 12** – Take an extra turn (in addition to placing/replacing a token).
4. **Roll a 10 (Defensive Roll)** – Remove an opponent’s token (except on protected grey cells: 2s and 12s).
5. **Roll an 11 (Wild Roll)** – Place a token on any empty cell.

---

## 📱 Features
- Interactive 6x6 game board
- Dice rolling animation & logic
- Full rules implemented, including extra turns and defensive/wild rolls
- Supports **2–4 players** (team play included)
- Dynamic token placement & removal
- Win condition checking

---

## 🛠 Tech Stack
- **Language**: Java  
- **Framework**: Android SDK  
- **IDE**: Android Studio / IntelliJ IDEA

---

## 🚀 Installation & Running
1. Clone the repository:
   ```bash
   git clone https://github.com/garethmunnings/SequenceDiceAndroid
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical Android device.
