package io.kamel.samples

public fun generateRandomImageUrl(seed: Int = (1..1000).random()): String = "https://picsum.photos/seed/$seed/500/500"