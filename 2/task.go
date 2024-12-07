package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func check(e error) {
	if e != nil {
		panic(e)
	}
}

func readListFromFile(filename string) [][]int {
	var list [][]int
	file, err := os.Open(filename)
	check(err)
	defer file.Close()

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		if scanner.Text() == "" {
			continue
		}
		split := strings.Split(scanner.Text(), " ")
		var numList []int
		for _, string := range split {
			numList = append(numList, convertToInteger(string))
		}
		list = append(list, numList)
	}

	return list
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

func isListValid(numlist []int) bool {
	decrease := false
	increase := false
	for i := 1; i < len(numlist); i++ {
		prev := numlist[i-1]
		curr := numlist[i]
		if prev == 0 && i == 1 {
			continue
		} else if prev == 0 {
			prev = numlist[i-2]
		} else if curr == 0 {
      continue
    }
		delta := prev - curr

		if absolute(delta) > 3 || delta == 0 {
			return false
		} else if delta > 0 {
			decrease = true
		} else if delta < 0 {
			increase = true
		}

		if increase && decrease {
			return false
		}
	}
	return true
}

func main() {
	list := readListFromFile("input.txt")

	totalSafe := 0

	for _, numlist := range list {
		fmt.Println(numlist)
		if isListValid(numlist) {
			totalSafe++
			fmt.Println("is safe")
		} else {
			for i := 0; i < len(numlist); i++ {
				changedList := make([]int, len(numlist))
				copy(changedList, numlist)
				changedList[i] = 0
        fmt.Println("permutation: ", changedList)
				if isListValid(changedList) {
					totalSafe++
					fmt.Println("is safe")
					break
				}
			}
		}
	}
	fmt.Println(totalSafe)
}
