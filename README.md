[![Build Status](https://travis-ci.org/yandex-money-tech/moira-kotlin-client.svg?branch=master)](https://travis-ci.org/yandex-money-tech/moira-kotlin-client)
[![Build status](https://ci.appveyor.com/api/projects/status/vwf2hvs36150upwx?svg=true)](https://ci.appveyor.com/project/f0y/moira-kotlin-client)
[![codecov](https://codecov.io/gh/yandex-money-tech/moira-kotlin-client/branch/master/graph/badge.svg)](https://codecov.io/gh/yandex-money-tech/moira-kotlin-client)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Javadoc](https://img.shields.io/badge/javadoc-latest-blue.svg)](https://yandex-money-tech.github.io/moira-kotlin-client/)
[![Download](https://api.bintray.com/packages/yandex-money-tech/maven/moira-kotlin-client/images/download.svg) ](https://bintray.com/yandex-money-tech/maven/moira-kotlin-client/_latestVersion)

# Yandex.Money Kotlin Moira Client

Client for [Moira](https://github.com/moira-alert/moira) written in Kotlin.

This is Kotlin implementation of the official [Python client](https://github.com/moira-alert/python-moira-client).

## Installation

To install client just add it as a dependency to your build tool.

Gradle example:

```groovy
dependencies {
    implementation 'com.yandex.money.tech:moira-kotlin-client:1.0.2'
}
```

## Getting started

First of all you have to initialize instance of Moira client: 
```kotlin
import ru.yandex.money.moira.client.Moira
import ru.yandex.money.moira.client.settings.MoiraSettings

val moira = Moira(
    MoiraSettings(
        baseUrl = "http://yourmoira.com/api"
    )
)
```

In addition you can specify user login and credentials to authorize operations in Moira via `MoiraSettings`. For more 
information about security in Moira see [this page](https://moira.readthedocs.io/en/latest/installation/security.html).

## User Guide

### Triggers

#### Create new trigger
```kotlin
import ru.yandex.money.moira.client.triggers.Trigger
import ru.yandex.money.moira.client.triggers.TriggerState.ERROR
import ru.yandex.money.moira.client.triggers.TriggerType.RISING
import ru.yandex.money.moira.client.triggers.expression.SimpleExpression
import ru.yandex.money.moira.client.triggers.ttl.TtlCondition

val triggerId = moira.triggers.create(
    trigger = Trigger(
        id = "service_trigger_name",
        name = "Trigger Name",
        description = "My trigger",
        tags = listOf("service"),
        targets = listOf("prefix.service.*.postfix"),
        triggerExpression = SimpleExpression(
            triggerType = RISING,
            warnValue = 300.0,
            errorValue = 600.0
        ),
        ttlCondition = TtlCondition(state = ERROR)
    )
)

println(triggerId) // 'service_trigger_name'
```
> **Note:** id parameter is not required but highly recommended for large production solutions.
> If parameter is not specified, random trigger guid will be generated.

### Update triggers
For example, turn off all triggers for Monday.
```kotlin
import java.time.DayOfWeek

moira.triggers.fetchAll()
    .map { 
        val newDays = it.schedule.days.map { day -> 
            if (day.dayOfWeek == DayOfWeek.MONDAY) day.copy(enabled = false) else day 
        }
        val newSchedule = it.schedule.copy(days = newDays)
        it.copy(schedule = newSchedule)
    }.forEach { moira.triggers.update(it) }
```

### Delete trigger
```kotlin
val trigger = moira.triggers.fetchById(id = "bb1a8514-128b-406e-bec3-25e94153ab30")
if (trigger != null) {
    if (moira.triggers.delete(id = trigger.id!!)) {
        println("Trigger deleted")
    }
}
```

### Check whether trigger exists or not (manually)
```kotlin
import ru.yandex.money.moira.client.triggers.Trigger
import ru.yandex.money.moira.client.triggers.TriggerType.RISING
import ru.yandex.money.moira.client.triggers.expression.SimpleExpression

val trigger = Trigger(
    name = "Trigger name",
    targets = listOf("service.rps"),
    tags = listOf("ops"),
    triggerExpression = SimpleExpression(
        triggerType = RISING,
        warnValue = 300.0,
        errorValue = 600.0
    )
)

val existingTriggerWithSameName = moira.triggers.fetchAll().find { it.name == trigger.name }
if (existingTriggerWithSameName == null) {
    val triggerId = moira.triggers.create(trigger = trigger)
    println(triggerId) // randomly generated GUID by Moira
}
```

## Subscription

### Create subscription
```kotlin
import ru.yandex.money.moira.client.subscriptions.Subscription

val subscriptionId = moira.subscriptions.create(subscription = Subscription(
    contacts = listOf("79ac9de2-a3b3-4f94-b3ea-74f6f4094fd2"),
    tags = listOf("tag")
))
    
println(subscriptionId) // random generated GUID by Moira
```

### Delete subscription
Delete all subscriptions
```kotlin
moira.subscriptions.fetchAll().forEach { moira.subscriptions.delete(id = it.id!!) }
```

## Contact

### Get all contacts
```kotlin
moira.contacts.all().forEach { println(it.id) }
```

### Get contact id by type and value
```kotlin
import ru.yandex.money.moira.client.contacts.Contacts

fun Contacts.fetchId(type: String, value: String): String? {
    return all().find { it.type == type && it.value == value }?.id
}

val contactId = moira.contacts.fetchId(type = "slack", value = "#err")
println(contactId)
```
