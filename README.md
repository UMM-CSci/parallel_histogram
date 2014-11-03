parallel_histogram
==================

Various approaches to parallel (threaded) histogram generation based on the ideas in http://www.futurechips.org/tips-for-power-coders/writing-optimizing-parallel-programs-complete.html

I (Nic) had thought that we might be able to do something cool with this to bring parallelism a little more clearly into the course, moving past just the craziness of Java threads. I never got far enough to ever use this in class in any way, but I still think there's a good idea lurking in here somewhere.

My plan was to start by doing this in straight Java with threads, and then switch over to some other 
languages like Clojure. To date, all that's ever happened is the Java bit, but other people are welcome to
share other implementations. We'd probably need to re-arrange the directory structures if we do that, but that's
not a big deal.
