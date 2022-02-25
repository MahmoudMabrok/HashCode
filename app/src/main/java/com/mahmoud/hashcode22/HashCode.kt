package com.mahmoud.hashcode22

import java.io.File

fun main() {
    val fileName = "e.txt"
    val data = File(fileName).readLines().toMutableList().apply {
        removeAt(0)
    }
    val processor = Processor()
    data.forEachIndexed { index, line ->
        processor.process(line, index % 2 == 0)
    }

    val outputFile = File("Output_$fileName").apply {
        createNewFile()
    }

    outputFile.writeText(processor.formatOutput())
}

class Processor {
    val data = mutableMapOf<String, Gradient>()

    fun process(input: String, isLike: Boolean = true) {
        val items = input.split(" ").toMutableList().apply {
            // remove first item which represent size of input
            removeAt(0)
        }

        items.forEach { itemName ->
            val item = data.getOrPut(itemName) {
                Gradient(itemName)
            }
            item.update(isLike)
        }

    }

    fun formatOutput(): String {
        val gradients = data.filter { it.value.likes > it.value.dislikes }.map { it.key }
        return "${gradients.size} ${gradients.joinToString(separator = " ")}"
    }

}

data class Gradient(val name: String, var likes: Int = 0, var dislikes: Int = 0) {
    fun update(isLike: Boolean) {
        if (isLike) likes += 1 else dislikes += 1
    }
}