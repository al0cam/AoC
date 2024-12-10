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

func searchHorizontally(rowIndex int, colIndex int, lines [][]string) int {
	total := 0

	if colIndex-3 >= 0 {
		wordLeft := ""
		for col := range len("xmas") {
			wordLeft += lines[rowIndex][colIndex-col]
		}
		if wordLeft == "XMAS" {
			total++
		}
	}
	// is xmas down
	if colIndex+3 < len(lines) {
		wordRight := ""
		for col := range len("xmas") {
			wordRight += lines[rowIndex][colIndex+col]
		}
		if wordRight == "XMAS" {
			total++
		}
	}

	if total != 0 {
		fmt.Println("horizontal: ", total)
	}
	return total
}

func searchVertically(rowIndex int, colIndex int, lines [][]string) int {
	total := 0
	// is xmas up
	if rowIndex-3 >= 0 {
		wordUp := ""
		for row := range len("xmas") {
			wordUp += lines[rowIndex-row][colIndex]
		}
		if wordUp == "XMAS" {
			total++
		}
	}
	// is xmas down
	if rowIndex+3 < len(lines) {
		wordDown := ""
		for row := range len("xmas") {
			wordDown += lines[rowIndex+row][colIndex]
		}
		if wordDown == "XMAS" {
			total++
		}
	}
	// incremet total

	if total != 0 {
		fmt.Println("vertical: ", total)
	}
	return total
}

func searchDiagonally(rowIndex int, colIndex int, lines [][]string) int {
	total := 0

	wordUpLeft := ""
	wordUpRight := ""
	if rowIndex-3 >= 0 && ( colIndex-3 >= 0 || colIndex+3 < len(lines[rowIndex]) ) {
		for index := range len("xmas") {
      // fmt.Println("indices: ", rowIndex, colIndex)
      if colIndex-3 >= 0 {
        wordUpLeft += lines[rowIndex-index][colIndex-index]
      }
      if colIndex+3 < len(lines[rowIndex]) {
        wordUpRight += lines[rowIndex-index][colIndex+index]
      }
		}
		if wordUpLeft == "XMAS" {
      fmt.Println("upLeft", wordUpLeft, wordUpRight)
			total++
		}
		if wordUpRight == "XMAS" {
      fmt.Println("upRight")
			total++
		}
	}

	wordDownLeft := ""
	wordDownRight := ""
  if rowIndex+3 < len(lines) && ( colIndex-3 >= 0 || colIndex+3 < len(lines[rowIndex]) ) {
		for index := range len("xmas") {
      if colIndex-3 >= 0 {
        wordDownLeft += lines[rowIndex+index][colIndex-index]
      }
      if colIndex+3 < len(lines[rowIndex]) {
        wordDownRight += lines[rowIndex+index][colIndex+index]
      }
		}
		if wordDownLeft == "XMAS" {
      fmt.Println("downLeft")
			total++
		}
		if wordDownRight == "XMAS" {
      fmt.Println("downRight")
			total++
		}
	}

	if total != 0 {
		fmt.Println("diag: ", total)
	}
	return total
}

func countXmas(rowIndex int, colIndex int, lines [][]string) int {
	// search hozirontally
	total := 0
	total += searchHorizontally(rowIndex, colIndex, lines)
	// search vertically
	total += searchVertically(rowIndex, colIndex, lines)
	// search diagonal
  total += searchDiagonally(rowIndex, colIndex, lines)
	return total
}

func main() {
	lines := readListFromFile("input.txt")
	// lines := readEverythingFromFile("input.txt")
	total := 0

	for i, row := range lines {
		fmt.Println(i, row)
	}
	fmt.Println()
	for rowIndex, row := range lines {
		fmt.Println(rowIndex, row)
		for colIndex, letter := range row {
			if letter == "X" {
				total += countXmas(rowIndex, colIndex, lines)
			}
		}
	}

	fmt.Println(total)
}
