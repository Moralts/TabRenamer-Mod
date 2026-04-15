package com.tabrenamer.moralts.client

object ClientRuleStore {
    private val rules = mutableMapOf<String, String>()
    var enableRules: Boolean = true
        private set

    fun updateRules(newRules: Map<String, String>) {
        rules.clear()
        rules.putAll(newRules)
    }

    fun setEnableRules(value: Boolean) {
        enableRules = value
    }

    fun getReplacement(playerName: String): String? = rules[playerName]

    fun clear() {
        rules.clear()
        enableRules = true
    }
}
