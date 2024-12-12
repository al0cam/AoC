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

func insertBefore(toInsert int, insertBefore int, numList []int) []int {
	newArray := numList
	// maybe extract this logic into the method above
	for i, num := range numList {
		if insertBefore == num {
			newArray = append(newArray, 0)
			copy(newArray[i+1:], newArray[i:])
			newArray[i] = toInsert
		}
	}
	return newArray
}

func insertAfter(toInsert int, insertAfter int, numList []int) []int {
	newArray := numList
	// maybe extract this logic into the method above
	for i, num := range numList {
		if insertAfter == num {
			newArray = append(newArray, 0)
			copy(newArray[i+1:], newArray[i:])
			newArray[i+1] = toInsert
		}
	}
	return newArray
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
    fmt.Println(rule, " is breaking the flow")
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

func isListWellOrdered(rules []rule, list []int) bool {
	relevantRules := makeRelevantRules(rules, list)
	for _, rule := range relevantRules {
		if !isRuleValid(rule, list) {
			return false
		}
	}
	return true
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

	for _, numList := range list {
		fmt.Println(numList)
		if isListWellOrdered(rules, numList) {
      middleMan := getTheMiddleMan(numList)
      fmt.Println(middleMan)
			total +=middleMan
		}
	}
	fmt.Println(total)
}
