package com.mahmoud.hashcode22

import java.io.File
import java.lang.StringBuilder
import java.util.*

fun main() {
    listOf("a", "b", "c", "d", "e", "f").forEach {
        val fileName = "$it.txt"

        val data = File(fileName)
        val sc = Scanner(data)

        val processor = Processor2()
        processor.process(sc)
        val result = processor.formatOutput()

        val outputFile = File("Output_$fileName").apply {
            createNewFile()
        }

        outputFile.writeText(result)
    }


}

class Processor2 {
    val contributors = mutableListOf<Contributor>()
    val projects = mutableListOf<Project>()
    var nProjects = 0

    fun process(sc: Scanner) {
        processContributors(sc)
        processProjects(sc)
    }

    fun processContributors(sc: Scanner) {
        val nContributors = sc.nextInt()
        nProjects = sc.nextInt()
        var remainC = nContributors
        while (remainC > 0) {
            val name = sc.next()
            val sk = sc.nextInt()
            val skills = mutableListOf<Skill>()
            for (i in 1..sk) {
                val skillName = sc.next()
                val level = sc.nextInt()
                skills.add(Skill(skillName, level))
            }
            contributors.add(Contributor(name, skills))
            remainC -= 1
        }
    }

    fun processProjects(sc: Scanner) {
        var remainProjects = nProjects
        while (remainProjects > 0) {
            val name = sc.next()
            val days = sc.nextInt()
            val award = sc.nextInt()
            val beforeBest = sc.nextInt()
            val rolesNumber = sc.nextInt()
            val skills = mutableListOf<Skill>()
            for (i in 1..rolesNumber) {
                val skillName = sc.next()
                val level = sc.nextInt()
                skills.add(Skill(skillName, level))
            }
            projects.add(Project(name, days, award, beforeBest, rolesNumber, skills))
            remainProjects -= 1
        }
    }

    fun formatOutput(): String {
        val result = mutableListOf<Project>()

        projects.forEach { project ->
            val toAssigneContributors = mutableSetOf<Contributor>()
            project.roles.forEach { role ->
                val contributor = contributors.find { it.canTakeRole(role) }
                if (contributor != null) {
                    role.isAssigned = true
                    contributor.assignedRole = role
                    toAssigneContributors.add(contributor)
                }
            }
            if (toAssigneContributors.toSet().size == project.rolesNumber) {
                assigneContiruntuers(toAssigneContributors, project)
                result.add(project)
            } else {
                project.roles
                    .filter { !it.isAssigned }
                    .forEach { role ->
                    val hasLowerTHan = contributors.find { it.canBeMentee(role) }
                    hasLowerTHan?.let {
                        val hasMentor = toAssigneContributors.filterNot { it.assignedRole == role }
                            .filter { it.canTakeRole(role) }

                        if (hasMentor.isNotEmpty()) {
                            hasLowerTHan.assignedRole = role
                            toAssigneContributors.add(hasLowerTHan)
                        }
                    }

                }
                if (toAssigneContributors.size == project.rolesNumber) {
                    assigneContiruntuers(toAssigneContributors, project)
                    val unique = project.assignedContrinuters.toSet().size == project.assignedContrinuters.size
                    if (unique)
                    result.add(project)
                }

            }
        }

        val aaa = projects.subtract(result)

        aaa.forEach { project ->
            val toAssigneContributors = mutableSetOf<Contributor>()
            project.roles.forEach { role ->
                val contributor = contributors.find { it.canTakeRole(role) }
                if (contributor != null) {
                    role.isAssigned = true
                    contributor.assignedRole = role
                    toAssigneContributors.add(contributor)
                }
            }
            if (toAssigneContributors.toSet().size == project.rolesNumber) {
                assigneContiruntuers(toAssigneContributors, project)
                result.add(project)
            } else {
                project.roles
                    .filter { !it.isAssigned }
                    .forEach { role ->
                        val hasLowerTHan = contributors.find { it.canBeMentee(role) }
                        hasLowerTHan?.let {
                            val hasMentor = toAssigneContributors.filterNot { it.assignedRole == role }
                                .filter { it.canTakeRole(role) }

                            if (hasMentor.isNotEmpty()) {
                                hasLowerTHan.assignedRole = role
                                toAssigneContributors.add(hasLowerTHan)
                            }
                        }

                    }
                if (toAssigneContributors.size == project.rolesNumber) {
                    assigneContiruntuers(toAssigneContributors, project)
                    result.add(project)
                }

            }
        }

        val otSize = result.size
        val sb = StringBuilder()
        val demileter = "\n"
        sb.append(otSize)
        sb.append(demileter)
        result.forEach{
            sb.append(it.name)
            sb.append(demileter)
            sb.append(it.assignedContrinuters.joinToString(separator = " ") { it.name })
            sb.append(demileter)
        }

        return sb.toString()
    }

    private fun assigneContiruntuers(
        toAssigneContributors: MutableSet<Contributor>,
        project: Project
    ) {
        project.assignedContrinuters.addAll(toAssigneContributors)
        toAssigneContributors.forEach {
            it.updateLevel()
        }
    }


}

data class Contributor(val name: String, val skills: List<Skill>, var assignedRole: Skill? = null) {
    fun canTakeRole(role: Skill): Boolean {
        return skills.any { it.name == role.name && it.level >= role.level }
    }

    fun canBeMentee(role: Skill): Boolean {
        return skills.any { it.name == role.name && it.level == role.level - 1 }
    }

    fun updateLevel() {
        val a = skills.find { it.name == assignedRole?.name } ?: return
        a.level = a.level + 1
    }
}

data class Skill(val name: String, var level: Int = 0, var isAssigned: Boolean = false)

data class Project(
    val name: String,
    val completetionsDays: Int = 0,
    val award: Int = 0,
    val bestDays: Int = 0,
    val rolesNumber: Int = 0,
    val roles: List<Skill>,
    val assignedContrinuters: MutableList<Contributor> = mutableListOf()
)