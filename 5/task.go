package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
	"strings"
)

func check(e error) {
	if e != nil {
		panic(e)
	}
}

func convertToInteger(number string) int {
	int, err := strconv.Atoi(number)
	check(err)
	return int
}

type rule struct {
	firstNumber  int
	secondNumber int
}

func readListFromFile(filename string) ([]rule, [][]int) {
	var rules []rule
	var list [][]int
	file, err := os.Open(filename)
	check(err)
	defer file.Close()

	ruleRegex, err := regexp.Compile(`\d{1,3}\|\d{1,3}`)
	check(err)

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		var numList []int
		if scanner.Text() == "" {
			continue
		}
		if ruleRegex.MatchString(scanner.Text()) {
			split := strings.Split(scanner.Text(), "|")
			rules = append(rules, rule{
				firstNumber:  convertToInteger(split[0]),
				secondNumber: convertToInteger(split[1]),
			})
		} else {
			split := strings.Split(scanner.Text(), ",")
			for _, string := range split {
				numList = append(numList, convertToInteger(string))
			}
			list = append(list, numList)
		}
	}

	return rules, list
}

func contains(numberToBeContained int, array []int) bool {
	for _, num := range array {
		if numberToBeContained == num {
			return true
		}
	}
	return false
}

func getIndex(num int, array []int) int {
	for i, number := range array {
		if num == number {
			return i
		}
	}
	return -1
}

func isRuleValid(rule rule, list []int) bool {
	indexOfFirstNumber := getIndex(rule.firstNumber, list)
	indexOfSecondNumber := getIndex(rule.secondNumber, list)
	if indexOfSecondNumber < indexOfFirstNumber {
		// fmt.Println(rule, " is breaking the flow")
		return false
	} else {
		return true
	}
}

func makeRelevantRules(allRules []rule, list []int) []rule {
	var relevantRules []rule
	for _, rule := range allRules {
		if contains(rule.firstNumber, list) && contains(rule.secondNumber, list) {
			relevantRules = append(relevantRules, rule)
		}
	}
	return relevantRules
}

func isListWellOrdered(relevantRules []rule, list []int) bool {
	for _, rule := range relevantRules {
		if !isRuleValid(rule, list) {
			return false
		}
	}
	return true
}

func swapPlaces(number1 int, number2 int, list []int) []int {
	indexOfFirstNumber := getIndex(number1, list)
	indexOfSecondNumber := getIndex(number2, list)
	swappedPlacesList := list
	swappedPlacesList[indexOfFirstNumber] = number2
	swappedPlacesList[indexOfSecondNumber] = number1
	return swappedPlacesList
}

func fixTheList(relevantRules []rule, list []int) []int {
	fixedList := list
  numberOfRuns := 0
	for !isListWellOrdered(relevantRules, list) {
    numberOfRuns++
    fmt.Println(numberOfRuns)
		for _, rule := range relevantRules {
			if !isRuleValid(rule, fixedList) {
				fixedList = swapPlaces(rule.firstNumber, rule.secondNumber, list)
			}
		}
	}
	return fixedList
}

func getTheMiddleMan(numList []int) int {
	return numList[len(numList)/2]
}

func main() {
	rules, list := readListFromFile("input.txt")
	for _, rule := range rules {
		fmt.Println(rule)
	}
	fmt.Println()
	for _, numList := range list {
		fmt.Println(numList)
	}
	fmt.Println()

	total := 0
	totalOfCorrected := 0

	for _, numList := range list {
		relevantRules := makeRelevantRules(rules, numList)
		fmt.Println("rules: ", relevantRules)
		fmt.Println("list: ", numList)
		if isListWellOrdered(relevantRules, numList) {
			middleMan := getTheMiddleMan(numList)
			total += middleMan
		} else {
			newList := fixTheList(relevantRules, numList)
			fmt.Println("newline: ", newList)
			middleMan := getTheMiddleMan(newList)
			fmt.Println(middleMan)
			totalOfCorrected += middleMan
		}
		fmt.Println()
	}
	fmt.Println(total)
	fmt.Println(totalOfCorrected)
}
