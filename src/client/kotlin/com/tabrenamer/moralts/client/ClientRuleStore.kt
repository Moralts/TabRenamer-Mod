package com.tabrenamer.moralts.client

object ClientRuleStore {
    private val rules = mutableMapOf<String, String>()

    fun updateRules(newRules: Map<String, String>) {
        rules.clear()
        rules.putAll(newRules)
    }

    fun getReplacement(playerName: String): String? = rules[playerName]

    fun clear() {
        rules.clear()
    }
}
