package org.testadirapa.sesterzo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform