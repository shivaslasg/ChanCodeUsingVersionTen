package com.example.chancodeusingversionten

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.style.layers.generated.rasterLayer
import com.mapbox.maps.extension.style.sources.generated.rasterSource
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.plugin.animation.MapAnimationOptions.Companion.mapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager

var mapView: MapView? = null

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        mapView = MapView(this)
//        setContentView(mapView)
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.map_view)

        //original
//        mapView?.getMapboxMap()?.loadStyleUri(
//           "https://maps-json.onemap.sg/Default.json"
//        ) {
//            addAnnotationToMap()
//        }

//Suggestion 1:   load OneMap BaseMap style using  loadstylejson() function and pass your base style json as string
//        mapView?.getMapboxMap()?.loadStyleJson("{\n" +
//                "\"version\":10,\n" +
//                "\"name\":\"Default\",\n" +
//                "\"sprite\":\"mapbox://sprites/sla/cj7u5gsz51v7n2ss6fx5nt4bt?refresh=true\",\n" +
//                "\"glyphs\":\"mapbox://fonts/sla/{fontstack}/{range}.pbf?refresh=true\",\n" +
//                "\"sources\":{\n" +
//                "\"Default\":{\n" +
//                "\"type\":\"raster\",\n" +
//                "\"tiles\":[\n" +
//                "\"https://maps-a.onemap.sg/v3/Default_HD/{z}/{x}/{y}.png?refresh=true\",\n" +
//                "\"https://maps-b.onemap.sg/v3/Default_HD/{z}/{x}/{y}.png?refresh=true\",\n" +
//                "\"https://maps-c.onemap.sg/v3/Default_HD/{z}/{x}/{y}.png?refresh=true\"\n" +
//                "],\n" +
//                "\"tileSize\":128,\n" +
//                "\"bounds\":[\n" +
//                "103.596,\n" +
//                "1.1443,\n" +
//                "104.4309,\n" +
//                "1.4835\n" +
//                "]\n" +
//                "}\n" +
//                "},\n" +
//                "\"layers\":[\n" +
//                "{\n" +
//                "\"id\":\"Default\",\n" +
//                "\"source\":\"Default\",\n" +
//                "\"type\":\"raster\"\n" +
//                "}\n" +
//                "]\n" +
//                "}") {
//            addAnnotationToMap()
//        }

//Suggestion 2:  save the style json as .json file in android assets folder and load using mapView.getMapboxMap().loadStyleUri()
//        mapView?.getMapboxMap()?.loadStyleUri("asset://Default.json") {
//            addAnnotationToMap()
//        }
//"https://maps-json.onemap.sg/Default.json"
//Suggestion 3: use both Mapbox Style (e.g Style.LIGHT) and OneMap style then add raster source separately once the style is loaded. here is the documentation and examples how you can work with rasterLayer.
        mapView?.getMapboxMap()?.loadStyle(
            style(Style.LIGHT) {
                +rasterSource("raster-source") {
                    tileSize(128L)
                    tiles(listOf("https://maps-a.onemap.sg/v3/Default_HD/{z}/{x}/{y}.png?refresh=true",
                                    "https://maps-b.onemap.sg/v3/Default_HD/{z}/{x}/{y}.png?refresh=true",
                                    "https://maps-c.onemap.sg/v3/Default_HD/{z}/{x}/{y}.png?refresh=true"))
                    minzoom(11L) // change according to the zoom level supported by OneMap raster source
                    maxzoom(20L)

                }
                +rasterLayer("raster-layer", "raster-source") {
                    addAnnotationToMap()
                }
            }
        )

    }

    private fun addAnnotationToMap() {
// Create an instance of the Annotation API and get the PointAnnotationManager.
        bitmapFromDrawableRes(
            this@MainActivity,
            R.drawable.red_marker
        )?.let {
            val annotationApi = mapView?.annotations
            val pointAnnotationManager = annotationApi?.createPointAnnotationManager(mapView!!)
// Set options for the resulting symbol layer.
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
// Define a geographic coordinate.
                .withPoint(Point.fromLngLat(103.8012641, 1.2739864))
// Specify the bitmap you assigned to the point annotation
// The bitmap will be added to map style automatically.
                .withIconImage(it)
// Add the resulting pointAnnotation to the map.
            pointAnnotationManager?.create(pointAnnotationOptions)

//            mapView?.getMapboxMap()?.flyTo(
//                cameraOptions {
//                    center(
//                        Point.fromLngLat(
//                            103.8012641,
//                            1.2739864
//                        )
//                    ) // Sets the new camera position on click point
//                    zoom(11.0) // Sets the zoom
//                },
//                mapAnimationOptions {
//                    duration(7000)
//                }
//            )
        }
    }


    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
// copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }
}