package main

import (
	"fmt"
	"os"
	"regexp"
	"strconv"
)

func check(e error) {
	if e != nil {
		panic(e)
	}
}

func readEverythingFromFile(filename string) string {
	file, err := os.ReadFile(filename)
	check(err)

	return string(file)
}

func convertToInteger(number string) int {
	int, err := strconv.Atoi(number)
	check(err)
	return int
}

func absolute(number int) int {
	if number >= 0 {
		return number
	} else {
		return -number
	}
}

func main() {
	// blockOfText := readEverythingFromFile("dummyInput.txt")
	blockOfText := readEverythingFromFile("input.txt")
	total := 0

	mulRegex, err := regexp.Compile(`(?:don\'t\(\))|(?:do\(\))|(?:mul\((?P<num1>\d{1,3}),(?P<num2>\d{1,3})\))`)
	check(err)

	matches := mulRegex.FindAllStringSubmatch(blockOfText, -1)

	// by default it's true, however, if the command don't() is hit, then it's disabled until the next do()
  permitToMultiply := true
	for _, match := range matches {
		fmt.Println(match)
		if match[0] == "don't()" {
			permitToMultiply = false
		} else if match[0] == "do()" {
			permitToMultiply = true
		} else if permitToMultiply {
			total += convertToInteger(match[1]) * convertToInteger(match[2])
		}
	}

	fmt.Println(total)
}
