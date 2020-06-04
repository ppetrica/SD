package sd.tema.T10

import hep.dataforge.meta.buildMeta
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import scientifik.plotly.Plotly
import scientifik.plotly.models.Trace
import scientifik.plotly.server.serve

@SpringBootApplication
@EnableScheduling
open class MonitoringApplication

val cpu = IntArray(100) { 0 }.toMutableList()

@KtorExperimentalAPI
fun main(args: Array<String>) {

    Thread(Runnable {
        val serverMeta = buildMeta {
            "update" to {
                "enabled" to true
            }
        }

         Plotly.serve(serverMeta) {
            val xs = (0..100).map { it.toDouble() / 100 }

            val ys = cpu

            val t = Trace.build(x = xs, y = ys) { name = "Usage" }

            plot {
                trace(t)

                layout {
                    title = "CPU Usage"
                    xaxis { title = "time" }
                    yaxis { title = "usage" }
                }
            }

            launch {
                while (isActive) {
                    delay(10)
                    t.y = cpu
                }
            }
        }
        readLine()
    }).start()
    runApplication<MonitoringApplication>(*args)
}
