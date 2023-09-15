package de.patternframeworks.busash

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BusashApplication

fun main(args: Array<String>) {
    runApplication<BusashApplication>(*args)
}
