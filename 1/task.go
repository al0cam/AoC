package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"slices"
	"strconv"
	"strings"
)

func check(e error) {
	if e != nil {
		panic(e)
	}
}

func readListsFromFile(filename string) ([]int, []int) {
	var list1, list2 []int
	file, err := os.Open(filename)
	check(err)
	defer file.Close()

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		if scanner.Text() == "" {
			continue
		}
		split := strings.Split(scanner.Text(), "   ")
		int, err := strconv.Atoi(split[0])
		check(err)
		list1 = append(list1, int)

		int, err = strconv.Atoi(split[1])
		check(err)
		list2 = append(list2, int)
	}

	return list1, list2
}

func main() {
	list1, list2 := readListsFromFile("input.txt")

	if !slices.IsSorted(list1) {
		slices.Sort(list1)
	}

	if !slices.IsSorted(list2) {
		slices.Sort(list2)
	}

	var sum int
	for i := range list1 {
		sum += int(math.Abs(float64(list1[i] - list2[i])))
	}

	fmt.Println(sum)

	// list3 contains numbers which will be checked for existance in list4
	// however, the number of occurences will be marked in the slice occurences which is of the same length as list3
	list3, list4 := readListsFromFile("input2.txt")

	occurences := make([]int, len(list3))
	for index, number := range list3 {
		for _, number2 := range list4 {


			if number == number2 {
				fmt.Println("number:", number)
				fmt.Println("number2:", number2)
				fmt.Println("occurences:", occurences[index])
				occurences[index]++
			}
		}
	}

	var total int
	for index, number := range list3 {
		total += number * occurences[index]
	}
	fmt.Println("the total is: " + strconv.Itoa(total))
}
