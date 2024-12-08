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
	blockOfText := readEverythingFromFile("input.txt")
	total := 0

	mulRegex, err := regexp.Compile(`mul\((?P<number1>\d{1,3}),(?P<number2>\d{1,3})\)`)
	check(err)

	matches := mulRegex.FindAllStringSubmatch(blockOfText, -1)

	for _, match := range matches {
		fmt.Println(match[1], match[2])
		total += convertToInteger(match[1]) * convertToInteger(match[2])
	}

	fmt.Println(total)
}
