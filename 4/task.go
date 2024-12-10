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
		split := strings.Split(scanner.Text(), "")
		var numList []string
		for _, string := range split {
			numList = append(numList, string)
		}
		list = append(list, numList)
	}

	return list
}

func searchHorizontally(rowIndex int, colIndex int, lines [][]string) bool {
	if colIndex-1 >= 0 && colIndex+1 < len(lines[rowIndex]) {
		masOrSam := lines[rowIndex][colIndex-1] + lines[rowIndex][colIndex] + lines[rowIndex][colIndex+1]
		if masOrSam == "MAS" || masOrSam == "SAM" {
			return true
		}
	}
	return false
}

func searchVertically(rowIndex int, colIndex int, lines [][]string) bool {
	if rowIndex-1 >= 0 && rowIndex+1 < len(lines[rowIndex]) {
		masOrSam := lines[rowIndex-1][colIndex] + lines[rowIndex][colIndex] + lines[rowIndex+1][colIndex]
		if masOrSam == "MAS" || masOrSam == "SAM" {
			return true
		}
	}
	return false
}

func search24Quadrants(rowIndex int, colIndex int, lines [][]string) bool {
	if (colIndex-1 >= 0 && colIndex+1 < len(lines[rowIndex])) && (rowIndex-1 >= 0 && rowIndex+1 < len(lines[rowIndex])) {
		masOrSam := lines[rowIndex-1][colIndex-1] + lines[rowIndex][colIndex] + lines[rowIndex+1][colIndex+1]
		if masOrSam == "MAS" || masOrSam == "SAM" {
			return true
		}
	}
	return false
}

func search13Quadrants(rowIndex int, colIndex int, lines [][]string) bool {
	if (colIndex-1 >= 0 && colIndex+1 < len(lines[rowIndex])) && (rowIndex-1 >= 0 && rowIndex+1 < len(lines[rowIndex])) {
		masOrSam := lines[rowIndex-1][colIndex+1] + lines[rowIndex][colIndex] + lines[rowIndex+1][colIndex-1]
		if masOrSam == "MAS" || masOrSam == "SAM" {
			return true
		}
	}
	return false
}

func isCross(rowIndex int, colIndex int, lines [][]string) bool {
  wordsAroundAFound := 0
  // if searchHorizontally(rowIndex, colIndex, lines){
  //   wordsAroundAFound++
  // }
  // if searchVertically(rowIndex, colIndex, lines) {
  //   wordsAroundAFound++
  // }
  if search13Quadrants(rowIndex, colIndex, lines){
    wordsAroundAFound++
  }
  if search24Quadrants(rowIndex, colIndex, lines){
    wordsAroundAFound++
  }
  if wordsAroundAFound > 1 {
    return true
  }
	return false
}

func main() {
	lines := readListFromFile("inputTask2.txt") // lines := readEverythingFromFile("input.txt")
	total := 0

	// for i, row := range lines {
	// 	fmt.Println(i, row)
	// }
	fmt.Println()
	for rowIndex, row := range lines {
		for colIndex, letter := range row {
			if letter == "A" && isCross(rowIndex, colIndex, lines) {
				total++
			}
		}
	}

	fmt.Println(total)
}
