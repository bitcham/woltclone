package cham.woltclone

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WoltcloneApplication

fun main(args: Array<String>) {
    runApplication<WoltcloneApplication>(*args)
}
