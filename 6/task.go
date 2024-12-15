package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

func check(e error) {
	if e != nil {
		panic(e)
	}
}

func readListFromFile(filename string) [][]string {
	var list [][]string
	file, err := os.Open(filename)
	check(err)
	defer file.Close()

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		if scanner.Text() == "" {
			continue
		}
		list = append(list, strings.Split(scanner.Text(), ""))
	}
	return list
}

func findGuardPosition(list [][]string) (int, int) {
	for row, values := range list {
		for col, value := range values {
			if value == "^" || value == ">" || value == "<" || value == "v" {
				return row, col
			}
		}
	}
	return -1, -1
}

func isSurfaceTraversible(row int, col int, grid [][]string) bool {
	if grid[row][col] == "#" {
		return false
	}
	return true
}

func makeStep(row int, col int, grid [][]string) (int, int, [][]string) {
	gridWithStep := grid
	guardPositionRow := row
	guardPositionCol := col
	guardDirection := grid[row][col]
	switch guardDirection {
	case "^":
		if row-1 >= 0 {
			if !isSurfaceTraversible(row-1, col, grid) {
				gridWithStep[row][col] = ">"
			} else {
				gridWithStep[row][col] = "X"
				gridWithStep[row-1][col] = "^"
				guardPositionRow = row - 1
			}
		} else {
			gridWithStep[row][col] = "X"
			guardPositionRow = -1
		}
	case ">":
		if col+1 < len(grid[row]) {
			if !isSurfaceTraversible(row, col+1, grid) {
				gridWithStep[row][col] = "v"
			} else {
				gridWithStep[row][col] = "X"
				gridWithStep[row][col+1] = ">"
				guardPositionCol = col + 1
			}
		} else {
			gridWithStep[row][col] = "X"
			guardPositionCol += 1
		}
	case "<":
		if col-1 >= 0 {
			if !isSurfaceTraversible(row, col-1, grid) {
				gridWithStep[row][col] = "^"
			} else {
				gridWithStep[row][col] = "X"
				gridWithStep[row][col-1] = "<"
				guardPositionCol = col - 1
			}
		} else {
			gridWithStep[row][col] = "X"
			guardPositionCol = -1
		}
	case "v":
		if row+1 < len(grid) {
			if !isSurfaceTraversible(row+1, col, grid) {
				gridWithStep[row][col] = "<"
			} else {
				gridWithStep[row][col] = "X"
				gridWithStep[row+1][col] = "v"
				guardPositionRow = row + 1
			}
		} else {
			gridWithStep[row][col] = "X"
			guardPositionRow += 1
		}
	}
	return guardPositionRow, guardPositionCol, gridWithStep
}

func traverseGrid(list [][]string) [][]string {
	traversedGrid := list
	guardRow, guardCol := findGuardPosition(list)
	for guardRow > -1 && guardCol > -1 && guardRow < len(list) && guardCol < len(list[guardRow]) {
		fmt.Print("\033[H\033[2J")
		for _, row := range traversedGrid {
			fmt.Println(row)
		}
		guardRow, guardCol, traversedGrid = makeStep(guardRow, guardCol, list)
	}
	fmt.Print("\033[H\033[2J")
	for _, row := range traversedGrid {
		fmt.Println(row)
	}
	return traversedGrid
}

func main() {
	list := readListFromFile("input.txt")
	for _, row := range list {
		fmt.Println(row)
	}
	traversedList := traverseGrid(list)
	totalX := 0
	for _, row := range traversedList {
		for _, letter := range row {
			if letter == "X" {
				totalX++
			}
		}
	}
	fmt.Println(totalX)
}
