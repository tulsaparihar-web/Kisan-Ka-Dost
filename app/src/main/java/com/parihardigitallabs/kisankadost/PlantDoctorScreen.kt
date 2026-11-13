package com.parihardigitallabs.kisankadost

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.parihardigitallabs.kisankadost.data.AppDatabase
import com.parihardigitallabs.kisankadost.data.PlantDiagnosisEntity
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val PlantGreen = Color(0xFF247A45)
private val PlantInk = Color(0xFF203128)
private val PlantMuted = Color(0xFF66766B)

/** Decode gallery images at a bounded size and apply the orientation stored by the camera. */
private fun decodePlantBitmap(context: Context, uri: Uri): Bitmap? {
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    val boundsStream = context.contentResolver.openInputStream(uri) ?: return null
    boundsStream.use { BitmapFactory.decodeStream(it, null, bounds) }
    if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

    val maxDimension = 1600
    var sampleSize = 1
    while (maxOf(bounds.outWidth / sampleSize, bounds.outHeight / sampleSize) > maxDimension) {
        sampleSize *= 2
    }
    val decoded = context.contentResolver.openInputStream(uri)?.use { stream ->
        BitmapFactory.decodeStream(stream, null, BitmapFactory.Options().apply {
            inSampleSize = sampleSize
            inPreferredConfig = Bitmap.Config.ARGB_8888
        })
    } ?: return null

    val rotation = context.contentResolver.openInputStream(uri)?.use { ExifInterface(it).rotationDegrees } ?: 0
    if (rotation == 0) return decoded
    val rotated = Bitmap.createBitmap(
        decoded, 0, 0, decoded.width, decoded.height,
        Matrix().apply { postRotate(rotation.toFloat()) }, true
    )
    if (rotated !== decoded) decoded.recycle()
    return rotated
}

private data class PlantGuide(
    val en: String, val hi: String, val category: String, val categoryHi: String,
    val lightEn: String, val lightHi: String, val waterEn: String, val waterHi: String,
    val soilEn: String, val soilHi: String, val careEn: String, val careHi: String
)

private val plantGuides = listOf(
    PlantGuide("Wheat", "गेहूँ", "Field crop", "खेत की फसल", "Full sun", "पूरा धूप", "Irrigate by soil moisture and crop stage; avoid waterlogging.", "मिट्टी की नमी और फसल अवस्था के अनुसार सिंचाई करें; जलभराव से बचें।", "Well-drained loam", "अच्छी जल निकासी वाली दोमट मिट्टी", "Use locally recommended varieties and follow the regional sowing calendar.", "स्थानीय अनुशंसित किस्म और क्षेत्रीय बुवाई कैलेंडर अपनाएँ।"),
    PlantGuide("Mustard", "सरसों", "Field crop", "खेत की फसल", "Full sun", "पूरा धूप", "Avoid moisture stress at flowering and pod formation.", "फूल और फलियों के समय नमी की कमी न होने दें।", "Well-drained loam", "अच्छी जल निकासी वाली दोमट मिट्टी", "Monitor aphids and diseases; seek local advice before treatment.", "माहू और रोगों की निगरानी करें; उपचार से पहले स्थानीय सलाह लें।"),
    PlantGuide("Pearl millet", "बाजरा", "Field crop", "खेत की फसल", "Full sun", "पूरा धूप", "Needs less water once established; avoid standing water.", "स्थापित होने के बाद कम पानी चाहिए; पानी खड़ा न रहने दें।", "Sandy loam", "बलुई दोमट", "Use quality seed and timely weeding.", "अच्छी गुणवत्ता का बीज और समय पर निराई करें।"),
    PlantGuide("Tomato", "टमाटर", "Vegetable", "सब्जी", "6–8 hours sun", "6–8 घंटे धूप", "Keep soil evenly moist; avoid wetting leaves late in the day.", "मिट्टी में समान नमी रखें; शाम को पत्तियाँ भिगोने से बचें।", "Rich, well-drained soil", "उपजाऊ, अच्छी जल निकासी वाली मिट्टी", "Stake plants, rotate crops and inspect lower leaves for spots.", "पौधों को सहारा दें, फसल चक्र अपनाएँ और निचली पत्तियों पर धब्बे देखें।"),
    PlantGuide("Chilli", "मिर्च", "Vegetable", "सब्जी", "Full sun", "पूरा धूप", "Water deeply when topsoil begins to dry.", "ऊपरी मिट्टी सूखने लगे तो गहराई से सिंचाई करें।", "Well-drained loam", "अच्छी जल निकासी वाली दोमट मिट्टी", "Watch for thrips, mites and leaf curl.", "थ्रिप्स, माइट और पत्ती मरोड़ की निगरानी करें।"),
    PlantGuide("Brinjal", "बैंगन", "Vegetable", "सब्जी", "Full sun", "पूरा धूप", "Maintain steady moisture, especially during fruiting.", "फल लगने के समय नियमित नमी बनाए रखें।", "Organic-rich loam", "जैविक पदार्थ वाली दोमट मिट्टी", "Inspect shoots and fruits for borer damage.", "तना और फल छेदक के लिए तनों और फलों की जाँच करें।"),
    PlantGuide("Okra", "भिंडी", "Vegetable", "सब्जी", "Full sun", "पूरा धूप", "Water during dry spells; avoid waterlogging.", "सूखे समय में पानी दें; जलभराव से बचें।", "Loose, well-drained soil", "भुरभुरी, अच्छी जल निकासी वाली मिट्टी", "Harvest tender pods regularly and keep the bed weed-free.", "कोमल फलियों की नियमित तुड़ाई करें और क्यारी को खरपतवार मुक्त रखें।"),
    PlantGuide("Coriander", "धनिया", "Herb", "हर्ब", "Gentle sun / partial shade", "हल्की धूप / आंशिक छाया", "Keep seedbed moist until germination.", "अंकुरण तक बीज क्यारी में नमी बनाए रखें।", "Loose, fertile soil", "भुरभुरी, उपजाऊ मिट्टी", "Sow in small batches for a steady supply of fresh leaves.", "ताजा पत्तियों के लिए थोड़े-थोड़े अंतराल पर बुवाई करें।"),
    PlantGuide("Mint", "पुदीना", "Herb", "हर्ब", "Partial sun", "आंशिक धूप", "Keep soil lightly moist, not soggy.", "मिट्टी में हल्की नमी रखें, बहुत गीली नहीं।", "Moist, rich potting mix", "नम, उपजाऊ गमले की मिट्टी", "Grow in a separate pot; pinch tips for bushier growth.", "अलग गमले में लगाएँ; घना बनाने के लिए ऊपरी भाग काटें।"),
    PlantGuide("Mango", "आम", "Fruit tree", "फलदार पेड़", "Full sun", "पूरा धूप", "Deep, less frequent watering after establishment.", "स्थापित पेड़ को कम बार गहरी सिंचाई दें।", "Deep, well-drained soil", "गहरी, अच्छी जल निकासी वाली मिट्टी", "Mulch around roots without touching the trunk; prune damaged branches.", "तने से दूर जड़ क्षेत्र में मल्च डालें; क्षतिग्रस्त शाखाएँ काटें।"),
    PlantGuide("Guava", "अमरूद", "Fruit tree", "फलदार पेड़", "Full sun", "पूरा धूप", "Water young plants regularly; reduce frequency in cool weather.", "नए पौधों को नियमित पानी दें; ठंड में अंतराल बढ़ाएँ।", "Well-drained loam", "अच्छी जल निकासी वाली दोमट मिट्टी", "Prune lightly after harvest and monitor fruit fly damage.", "तुड़ाई के बाद हल्की छँटाई करें और फल मक्खी की निगरानी करें।"),
    PlantGuide("Pomegranate", "अनार", "Fruit tree", "फलदार पेड़", "Full sun", "पूरा धूप", "Avoid irregular watering that can contribute to fruit cracking.", "अनियमित सिंचाई से बचें, इससे फल फट सकते हैं।", "Well-drained soil", "अच्छी जल निकासी वाली मिट्टी", "Maintain balanced moisture and remove diseased branches.", "संतुलित नमी रखें और रोगग्रस्त शाखाएँ हटाएँ।"),
    PlantGuide("Lemon", "नींबू", "Fruit tree", "फलदार पेड़", "6–8 hours sun", "6–8 घंटे धूप", "Water when upper soil begins to dry; do not saturate roots.", "ऊपरी मिट्टी सूखने लगे तो पानी दें; जड़ों में पानी न भरें।", "Slightly acidic, well-drained soil", "हल्की अम्लीय, अच्छी जल निकासी वाली मिट्टी", "Check new growth for leaf miner and nutrient symptoms.", "नई पत्तियों पर लीफ माइनर और पोषक तत्वों के लक्षण देखें।"),
    PlantGuide("Papaya", "पपीता", "Fruit tree", "फलदार पौधा", "Full sun", "पूरा धूप", "Regular moisture is important, but drainage must be excellent.", "नियमित नमी जरूरी है, लेकिन जल निकासी बहुत अच्छी होनी चाहिए।", "Rich, well-drained soil", "उपजाऊ, अच्छी जल निकासी वाली मिट्टी", "Protect the stem and avoid water collecting around the base.", "तने को चोट से बचाएँ और आधार के पास पानी जमा न होने दें।"),
    PlantGuide("Money plant", "मनी प्लांट", "Indoor plant", "इनडोर पौधा", "Bright indirect light", "तेज लेकिन अप्रत्यक्ष रोशनी", "Water when the top few centimetres of soil are dry.", "ऊपरी कुछ सेंटीमीटर मिट्टी सूखने पर पानी दें।", "Airy, well-drained potting mix", "हवादार, अच्छी जल निकासी वाला पॉटिंग मिक्स", "Trim long vines and wipe leaves; avoid standing water.", "लंबी बेलों की छँटाई करें और पत्तियाँ साफ करें; पानी जमा न होने दें।"),
    PlantGuide("Snake plant", "स्नेक प्लांट", "Indoor plant", "इनडोर पौधा", "Low to bright indirect light", "कम से तेज अप्रत्यक्ष रोशनी", "Let soil dry completely between watering.", "दो सिंचाइयों के बीच मिट्टी पूरी तरह सूखने दें।", "Cactus mix / sandy mix", "कैक्टस मिक्स / रेतीला मिश्रण", "Use a pot with drainage holes; avoid overwatering.", "छेद वाला गमला रखें; अधिक पानी देने से बचें।"),
    PlantGuide("Aloe vera", "एलोवेरा", "Medicinal / aromatic", "औषधीय / सुगंधित", "Bright light", "तेज रोशनी", "Water sparingly after soil dries.", "मिट्टी सूखने के बाद ही कम पानी दें।", "Sandy, fast-draining mix", "रेतीला, जल्दी पानी निकालने वाला मिश्रण", "Protect from prolonged waterlogging and extreme cold.", "लंबे समय तक जलभराव और अत्यधिक ठंड से बचाएँ।"),
    PlantGuide("Peace lily", "पीस लिली", "Indoor plant", "इनडोर पौधा", "Medium indirect light", "मध्यम अप्रत्यक्ष रोशनी", "Keep lightly moist; empty excess water from the tray.", "हल्की नमी रखें; प्लेट में अतिरिक्त पानी निकालें।", "Moist, airy potting mix", "नम, हवादार पॉटिंग मिक्स", "Keep away from harsh afternoon sun; toxic if chewed by pets.", "तेज दोपहर की धूप से बचाएँ; पालतू जानवर चबाएँ तो विषाक्त हो सकता है।"),
    PlantGuide("Areca palm", "अरेका पाम", "Indoor plant", "इनडोर पौधा", "Bright indirect light", "तेज अप्रत्यक्ष रोशनी", "Water when the top layer begins to dry.", "ऊपरी परत सूखने लगे तो पानी दें।", "Well-drained potting mix", "अच्छी जल निकासी वाला पॉटिंग मिक्स", "Wipe dusty fronds and avoid cold drafts.", "धूल भरी पत्तियाँ साफ करें और ठंडी हवा से बचाएँ।"),
    PlantGuide("Rose", "गुलाब", "Ornamental flower", "सजावटी फूल", "6+ hours sun", "6+ घंटे धूप", "Water at the base when upper soil dries.", "ऊपरी मिट्टी सूखने पर जड़ के पास पानी दें।", "Fertile, well-drained soil", "उपजाऊ, अच्छी जल निकासी वाली मिट्टी", "Prune for airflow and inspect for aphids and black spot.", "हवा के आवागमन के लिए छँटाई करें और माहू व काले धब्बों की जाँच करें।"),
    PlantGuide("Hibiscus", "गुड़हल", "Ornamental flower", "सजावटी फूल", "Full sun to partial shade", "पूरी धूप से आंशिक छाया", "Water regularly during active growth; avoid soggy roots.", "सक्रिय वृद्धि में नियमित पानी दें; जड़ों में पानी न भरने दें।", "Rich, well-drained soil", "उपजाऊ, अच्छी जल निकासी वाली मिट्टी", "Pinch tips to encourage branching and watch for whiteflies.", "शाखाएँ बढ़ाने के लिए ऊपरी भाग काटें और सफेद मक्खी देखें।"),
    PlantGuide("Marigold", "गेंदा", "Ornamental flower", "सजावटी फूल", "Full sun", "पूरा धूप", "Water when topsoil dries; avoid wet foliage overnight.", "ऊपरी मिट्टी सूखने पर पानी दें; रात में पत्तियाँ गीली न रखें।", "Well-drained garden soil", "अच्छी जल निकासी वाली बगीचे की मिट्टी", "Remove faded flowers to encourage more blooms.", "अधिक फूलों के लिए मुरझाए फूल हटाते रहें।"),
    PlantGuide("Bougainvillea", "बोगनवेलिया", "Ornamental flower", "सजावटी फूल", "Full sun", "पूरा धूप", "Allow soil to dry somewhat between watering.", "दो सिंचाइयों के बीच मिट्टी कुछ सूखने दें।", "Sandy, sharply drained soil", "रेतीली, तेज जल निकासी वाली मिट्टी", "Avoid excess nitrogen and prune after flowering as needed.", "अधिक नाइट्रोजन से बचें और जरूरत पर फूल आने के बाद छँटाई करें।"),
    PlantGuide("Jasmine", "चमेली", "Ornamental flower", "सजावटी फूल", "Sun to partial shade", "धूप से आंशिक छाया", "Keep evenly moist during active growth.", "सक्रिय वृद्धि के दौरान समान नमी रखें।", "Fertile, well-drained soil", "उपजाऊ, अच्छी जल निकासी वाली मिट्टी", "Support climbing types and prune lightly after flowering.", "बेल वाली किस्मों को सहारा दें और फूलों के बाद हल्की छँटाई करें।"),
    PlantGuide("Tulsi", "तुलसी", "Medicinal / aromatic", "औषधीय / सुगंधित", "4–6 hours sun", "4–6 घंटे धूप", "Water when topsoil begins to dry.", "ऊपरी मिट्टी सूखने लगे तो पानी दें।", "Well-drained potting mix", "अच्छी जल निकासी वाला पॉटिंग मिक्स", "Pinch flower spikes for leafy growth and provide airflow.", "अधिक पत्तियों के लिए फूलों की मंजरी तोड़ें और हवा का आवागमन रखें।"),
    PlantGuide("Neem", "नीम", "Tree", "पेड़", "Full sun", "पूरा धूप", "Young trees need establishment watering; mature trees tolerate dry periods.", "नए पेड़ों को शुरुआती सिंचाई चाहिए; बड़े पेड़ सूखा सह सकते हैं।", "Well-drained soil", "अच्छी जल निकासी वाली मिट्टी", "Give adequate space and avoid damaging main roots.", "पर्याप्त जगह दें और मुख्य जड़ों को नुकसान न पहुँचाएँ।"),
    PlantGuide("Lemongrass", "लेमनग्रास", "Herb", "हर्ब", "Full sun", "पूरा धूप", "Water regularly while establishing; reduce in cool weather.", "स्थापित होते समय नियमित पानी दें; ठंड में कम करें।", "Fertile, well-drained soil", "उपजाऊ, अच्छी जल निकासी वाली मिट्टी", "Divide clumps when crowded and harvest outer leaves.", "घना होने पर गुच्छों को बाँटें और बाहरी पत्तियों की कटाई करें।")
)

data class PlantPrediction(val label: String, val confidence: Float)
data class PlantDoctorState(val bitmap: Bitmap? = null, val predictions: List<PlantPrediction> = emptyList(), val analyzing: Boolean = false, val error: String? = null)

class PlantDoctorViewModel(application: Application) : AndroidViewModel(application) {
    private val diagnosisDao = AppDatabase.getInstance(application).plantDiagnosisDao()
    val history = diagnosisDao.observeAll()
    private val _state = MutableStateFlow(PlantDoctorState())
    val state = _state.asStateFlow()
    private var interpreter: Interpreter? = null
    private var labels: List<String> = emptyList()

    fun showError(message: String) { _state.value = _state.value.copy(analyzing = false, error = message) }

    fun deleteHistory(record: PlantDiagnosisEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { diagnosisDao.delete(record); File(record.imagePath).delete() }
        }
    }

    private suspend fun saveHistory(bitmap: Bitmap, prediction: PlantPrediction) {
        val imagePath = withContext(Dispatchers.IO) {
            val directory = File(getApplication<Application>().filesDir, "plant_diagnoses")
            if (!directory.exists() && !directory.mkdirs()) error("Unable to create diagnosis photo folder")
            val target = File.createTempFile("diagnosis_", ".jpg", directory)
            val maxSide = 1024
            val scale = minOf(1f, maxSide.toFloat() / maxOf(bitmap.width, bitmap.height).coerceAtLeast(1))
            val savedBitmap = if (scale < 1f) Bitmap.createScaledBitmap(bitmap, (bitmap.width * scale).toInt().coerceAtLeast(1), (bitmap.height * scale).toInt().coerceAtLeast(1), true) else bitmap
            try {
                target.outputStream().use { output -> check(savedBitmap.compress(Bitmap.CompressFormat.JPEG, 78, output)) { "Unable to save diagnosis photo" } }
            } catch (error: Exception) {
                target.delete(); throw error
            } finally {
                if (savedBitmap !== bitmap) savedBitmap.recycle()
            }
            target.absolutePath
        }
        try {
            diagnosisDao.insert(PlantDiagnosisEntity(imagePath = imagePath, predictedLabel = prediction.label, confidence = prediction.confidence))
        } catch (error: Exception) {
            File(imagePath).delete(); throw error
        }
    }

    fun analyze(bitmap: Bitmap) {
        _state.value = PlantDoctorState(bitmap = bitmap, analyzing = true)
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val model = interpreter ?: loadModel().also { interpreter = it }
                val inputShape = model.getInputTensor(0).shape()
                val outputShape = model.getOutputTensor(0).shape()
                require(inputShape.size == 4 && inputShape[0] == 1 && inputShape[3] == 3 && inputShape[1] > 0 && inputShape[2] > 0) { "Unsupported model input shape" }
                require(outputShape.size == 2 && outputShape[0] == 1 && outputShape[1] > 0) { "Unsupported model output shape" }
                require(model.getInputTensor(0).dataType() == org.tensorflow.lite.DataType.FLOAT32) { "Unsupported model input type" }
                require(model.getOutputTensor(0).dataType() == org.tensorflow.lite.DataType.FLOAT32) { "Unsupported model output type" }
                val height = inputShape[1]
                val width = inputShape[2]
                val outputCount = outputShape.last()
                if (labels.isEmpty()) {
                    labels = getApplication<Application>().assets.open("plant_disease_labels.txt")
                        .bufferedReader().useLines { it.filter(String::isNotBlank).toList() }
                }
                require(labels.size == outputCount) { "Model labels do not match model output." }

                // Center-crop to the model's square input while preserving the leaf's proportions.
                val cropSide = minOf(bitmap.width, bitmap.height)
                require(cropSide > 0) { "The selected image is empty." }
                val square = Bitmap.createBitmap(
                    bitmap, (bitmap.width - cropSide) / 2, (bitmap.height - cropSide) / 2,
                    cropSide, cropSide
                )
                val resized = Bitmap.createScaledBitmap(square, width, height, true)
                val output = Array(1) { FloatArray(outputCount) }
                try {
                    val pixels = IntArray(width * height)
                    resized.getPixels(pixels, 0, width, 0, 0, width, height)
                    val input = ByteBuffer.allocateDirect(4 * width * height * 3).order(ByteOrder.nativeOrder())
                    pixels.forEach { pixel ->
                        input.putFloat(((pixel shr 16) and 0xFF) / 255f)
                        input.putFloat(((pixel shr 8) and 0xFF) / 255f)
                        input.putFloat((pixel and 0xFF) / 255f)
                    }
                    input.rewind()
                    model.run(input, output)
                } finally {
                    if (resized !== square && resized !== bitmap) resized.recycle()
                    if (square !== bitmap) square.recycle()
                }
                val results = output[0].mapIndexed { index, score -> PlantPrediction(labels[index], score) }
                    .filter { it.confidence.isFinite() && it.confidence in 0f..1f }
                    .sortedByDescending { it.confidence }.take(3)
                results.firstOrNull()?.let { prediction -> runCatching { saveHistory(bitmap, prediction) } }
                withContext(Dispatchers.Main) { _state.value = PlantDoctorState(bitmap = bitmap, predictions = results) }
            } catch (error: Exception) {
                withContext(Dispatchers.Main) {
                    _state.value = PlantDoctorState(bitmap = bitmap, error = "ANALYSIS_FAILED")
                }
            }
        }
    }

    private fun loadModel(): Interpreter {
        val bytes = getApplication<Application>().assets.open("plant_disease_model.tflite").use { it.readBytes() }
        val buffer = ByteBuffer.allocateDirect(bytes.size).order(ByteOrder.nativeOrder())
        buffer.put(bytes)
        buffer.rewind()
        return Interpreter(buffer, Interpreter.Options().setNumThreads(2))
    }

    override fun onCleared() {
        interpreter?.close()
        interpreter = null
        super.onCleared()
    }
}

@Composable
fun PlantDoctorScreen(isHindi: Boolean, viewModel: PlantDoctorViewModel = viewModel()) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val history by viewModel.history.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableIntStateOf(0) }
    var search by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("All") }

    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) viewModel.analyze(bitmap)
    }
    val gallery = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) scope.launch(Dispatchers.IO) {
            val bitmap = runCatching { decodePlantBitmap(context, uri) }.getOrNull()
            withContext(Dispatchers.Main) {
                if (bitmap != null) viewModel.analyze(bitmap)
                else viewModel.showError(if (isHindi) "फोटो नहीं खुल सकी। दूसरी फोटो चुनें।" else "Could not open this photo. Please choose another image.")
            }
        }
    }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp), verticalArrangement = Arrangement.spacedBy(13.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(if (isHindi) "पौधा डॉक्टर" else "Plant Doctor", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = PlantInk)
                Text(if (isHindi) "पौधों की पहचान, देखभाल और पत्ती की जाँच" else "Plant care, growing guides and leaf health checks", color = PlantMuted, fontSize = 13.sp)
            }
            Icon(Icons.Default.Eco, contentDescription = null, tint = PlantGreen, modifier = Modifier.size(34.dp))
        }
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text(if (isHindi) "फोटो जाँच" else "Photo Doctor") })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text(if (isHindi) "पौधा गाइड" else "Plant Guide") })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text(if (isHindi) "पुरानी जाँच" else "History") })
        }

        if (selectedTab == 0) {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF3E9)), shape = RoundedCornerShape(15.dp)) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = PlantGreen)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (isHindi) "ऑफलाइन AI मॉडल 14 फसलों की 39 श्रेणियों तक सीमित है—सेब, ब्लूबेरी, चेरी, मक्का, अंगूर, संतरा, आड़ू, शिमला मिर्च, आलू, रास्पबेरी, सोयाबीन, स्क्वैश, स्ट्रॉबेरी और टमाटर। अन्य पौधों पर भरोसेमंद पहचान का दावा नहीं किया जाता। परिणाम अंतिम निदान नहीं है।"
                        else "The offline AI model is limited to 39 output classes across 14 crops: apple, blueberry, cherry, maize, grape, orange, peach, bell pepper, potato, raspberry, soybean, squash, strawberry and tomato. It does not reliably identify other plants. Results are not a confirmed diagnosis.",
                        color = PlantInk, fontSize = 12.sp, lineHeight = 17.sp
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(9.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { camera.launch(null) }, enabled = !state.analyzing, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(5.dp))
                    Text(if (isHindi) "फोटो लें" else "Take photo")
                }
                OutlinedButton(onClick = { gallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, enabled = !state.analyzing, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                    Spacer(Modifier.width(5.dp))
                    Text(if (isHindi) "गैलरी" else "Gallery")
                }
            }
            state.bitmap?.let { Image(it.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxWidth().heightIn(max = 250.dp)) }
            if (state.analyzing) {
                Card {
                    Column(Modifier.fillMaxWidth().padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(9.dp)) {
                        CircularProgressIndicator(color = PlantGreen)
                        Text(if (isHindi) "पत्ती की जाँच हो रही है…" else "Checking the leaf…", fontWeight = FontWeight.SemiBold)
                        Text(if (isHindi) "यह जाँच फोन पर ऑफलाइन होती है।" else "Analysis runs offline on this phone.", color = PlantMuted, fontSize = 12.sp)
                    }
                }
            }
            state.error?.let { error -> Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0E8))) {
                val message = if (error == "ANALYSIS_FAILED") {
                    if (isHindi) "फोटो की जाँच नहीं हो सकी। साफ फोटो चुनें और फिर प्रयास करें।"
                    else "Unable to analyse this image. Choose a clear photo and try again."
                } else error
                Text((if (isHindi) "जाँच पूरी नहीं हो सकी: " else "Analysis failed: ") + message, Modifier.padding(13.dp), color = Color(0xFF8A3D20), fontSize = 13.sp)
            } }
            if (state.predictions.isNotEmpty()) {
                val top = state.predictions.first()
                val background = top.label.contains("background", true)
                val low = top.confidence < 0.60f
                Card(shape = RoundedCornerShape(17.dp)) {
                    Column(Modifier.fillMaxWidth().padding(15.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                        Text(if (isHindi) "AI जाँच का परिणाम" else "AI scan result", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PlantInk)
                        Text(if (background) (if (isHindi) "स्पष्ट पत्ती नहीं मिली" else "No supported leaf detected") else displayDiseaseLabel(top.label, isHindi), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PlantGreen)
                        Text((if (isHindi) "मॉडल स्कोर: " else "Model score: ") + (top.confidence * 100).toInt().toString() + "%", color = PlantMuted, fontSize = 13.sp)
                        LinearProgressIndicator(progress = { top.confidence.coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth(), color = PlantGreen)
                        if (low || background) {
                            Text(if (isHindi) "विश्वास कम है। इसे पक्की बीमारी न मानें। एक साफ, पास से ली गई पत्ती की फोटो दोबारा लें।" else "Confidence is low. Do not treat this as a confirmed diagnosis. Retake a clear close-up photo of one leaf.", color = Color(0xFF8A5A16), fontSize = 13.sp)
                        } else {
                            Text(if (isHindi) "यह AI आधारित प्रारंभिक अनुमान है, अंतिम निदान नहीं।" else "This is an AI-assisted preliminary indication, not a confirmed diagnosis.", color = PlantMuted, fontSize = 12.sp)
                            Text(if (isHindi) "संभावित देखभाल के कदम" else "Suggested first care steps", fontWeight = FontWeight.Bold, color = PlantInk)
                            Text(recommendationFor(top.label, isHindi), color = PlantInk, fontSize = 13.sp, lineHeight = 19.sp)
                        }
                        if (state.predictions.size > 1) {
                            Divider()
                            Text(if (isHindi) "अन्य संभावित परिणाम" else "Other possible matches", fontWeight = FontWeight.SemiBold)
                            state.predictions.drop(1).forEach { p ->
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(displayDiseaseLabel(p.label, isHindi), modifier = Modifier.weight(1f), color = PlantMuted, fontSize = 12.sp)
                                    Text((p.confidence * 100).toInt().toString() + "%", color = PlantMuted, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
            Text(if (isHindi) "बेहतर परिणाम के लिए एक पत्ती को पास से, साफ रोशनी में और बिना धुंधलेपन के फोटो लें।" else "For best results, photograph one leaf close-up in good light and in focus.", color = PlantMuted, fontSize = 12.sp)
        } else if (selectedTab == 1) {
            OutlinedTextField(value = search, onValueChange = { search = it }, modifier = Modifier.fillMaxWidth(), singleLine = true, leadingIcon = { Icon(Icons.Default.Search, null) }, label = { Text(if (isHindi) "पौधे का नाम खोजें" else "Search plants") })
            val categories = listOf("All", "Field crop", "Vegetable", "Fruit tree", "Indoor plant", "Ornamental flower", "Medicinal / aromatic", "Herb")
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                categories.forEach { item ->
                    FilterChip(selected = category == item, onClick = { category = item }, label = { Text(if (isHindi) categoryHindi(item) else item) })
                }
            }
            val filtered = plantGuides.filter {
                (category == "All" || it.category == category || (category == "Medicinal / aromatic" && it.category.startsWith("Medicinal"))) &&
                    (search.isBlank() || it.en.contains(search, true) || it.hi.contains(search))
            }
            if (filtered.isEmpty()) Text(if (isHindi) "कोई पौधा नहीं मिला।" else "No plants found.", color = PlantMuted)
            filtered.forEach { guide ->
                Card(shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(if (isHindi) guide.hi else guide.en, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 17.sp, color = PlantInk)
                            AssistChip(onClick = { category = guide.category }, label = { Text(if (isHindi) guide.categoryHi else guide.category, fontSize = 10.sp) })
                        }
                        Text((if (isHindi) "धूप: " else "Light: ") + if (isHindi) guide.lightHi else guide.lightEn, color = PlantMuted, fontSize = 12.sp)
                        Text((if (isHindi) "पानी: " else "Water: ") + if (isHindi) guide.waterHi else guide.waterEn, color = PlantInk, fontSize = 13.sp)
                        Text((if (isHindi) "मिट्टी: " else "Soil: ") + if (isHindi) guide.soilHi else guide.soilEn, color = PlantInk, fontSize = 13.sp)
                        Text((if (isHindi) "देखभाल: " else "Care: ") + if (isHindi) guide.careHi else guide.careEn, color = PlantInk, fontSize = 13.sp, lineHeight = 18.sp)
                    }
                }
            }
        } else {
            if (history.isEmpty()) {
                Text(if (isHindi) "अभी तक कोई फोटो जाँच सहेजी नहीं गई है। जाँच पूरी होने पर उसका फोटो और परिणाम यहाँ दिखेगा।" else "No saved photo checks yet. Completed scans will appear here with their photo and result.", color = PlantMuted, fontSize = 13.sp)
            } else {
                Text(if (isHindi) "आपकी पिछली जाँचें इस फोन पर निजी रूप से सहेजी जाती हैं।" else "Your previous checks are stored privately on this phone.", color = PlantMuted, fontSize = 12.sp)
                history.forEach { record -> PlantDiagnosisHistoryCard(record = record, isHindi = isHindi, onDelete = { viewModel.deleteHistory(record) }) }
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun PlantDiagnosisHistoryCard(record: PlantDiagnosisEntity, isHindi: Boolean, onDelete: () -> Unit) {
    val thumbnail by produceState<Bitmap?>(initialValue = null, key1 = record.imagePath) {
        value = withContext(Dispatchers.IO) { BitmapFactory.decodeFile(record.imagePath) }
    }
    Card(shape = RoundedCornerShape(15.dp)) {
        Row(Modifier.fillMaxWidth().padding(11.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            thumbnail?.let { Image(it.asImageBitmap(), contentDescription = null, modifier = Modifier.size(76.dp), contentScale = ContentScale.Crop) }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(displayDiseaseLabel(record.predictedLabel, isHindi), color = PlantInk, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(SimpleDateFormat("dd MMM yyyy, hh:mm a", if (isHindi) Locale("hi", "IN") else Locale.getDefault()).format(Date(record.createdAt)), color = PlantMuted, fontSize = 11.sp)
                Text((if (isHindi) "मॉडल स्कोर: " else "Model score: ") + (record.confidence * 100).toInt().toString() + "%", color = PlantMuted, fontSize = 11.sp)
                Text(if (isHindi) "प्रारंभिक AI संकेत — अंतिम निदान नहीं" else "Preliminary AI indication — not a diagnosis", color = PlantMuted, fontSize = 10.sp)
            }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = if (isHindi) "हटाएँ" else "Delete", tint = PlantMuted) }
        }
    }
}

private fun displayDiseaseLabel(label: String, isHindi: Boolean): String {
    val crop = label.substringBefore(" ")
    val cropsHi = mapOf(
        "apple" to "सेब", "blueberry" to "ब्लूबेरी", "cherry" to "चेरी",
        "corn" to "मक्का", "grape" to "अंगूर", "orange" to "संतरा",
        "peach" to "आड़ू", "pepper" to "शिमला मिर्च", "potato" to "आलू",
        "raspberry" to "रास्पबेरी", "soybean" to "सोयाबीन", "squash" to "स्क्वैश",
        "strawberry" to "स्ट्रॉबेरी", "tomato" to "टमाटर"
    )
    val cropEn = when (crop) {
        "corn" -> "Maize"
        "pepper" -> "Bell pepper"
        else -> crop.replaceFirstChar { it.uppercase() }
    }
    if (label.contains("background", true)) return if (isHindi) "असमर्थित फोटो" else "Unsupported image"
    if (label.contains("healthy", true)) return if (isHindi) (cropsHi[crop] ?: cropEn) + " – स्वस्थ पत्ती" else cropEn + " – Healthy"

    val disease = label.substringAfter(" ")
        .replace("including sour", "")
        .replace("maize", "")
        .replace("bell", "")
        .trim()
    if (!isHindi) return cropEn + " – " + disease.replaceFirstChar { it.uppercase() }

    val diseaseHi = listOf(
        "cercospora leaf spot gray leaf spot" to "सर्कोस्पोरा / ग्रे लीफ स्पॉट",
        "leaf blight isariopsis leaf spot" to "पत्ती झुलसा (इसारियोप्सिस धब्बा)",
        "haunglongbing citrus greening" to "सिट्रस ग्रीनिंग (HLB)",
        "spider mites two spotted spider mite" to "दो-धब्बेदार मकड़ी माइट",
        "yellow leaf curl virus" to "पीला पत्ती मरोड़ विषाणु",
        "cedar apple rust" to "सीडर-एप्पल रतुआ",
        "northern leaf blight" to "उत्तरी पत्ती झुलसा",
        "esca black measles" to "एस्का (ब्लैक मीज़ल्स)",
        "septoria leaf spot" to "सेप्टोरिया पत्ती धब्बा",
        "powdery mildew" to "चूर्णिल आसिता",
        "bacterial spot" to "जीवाणु धब्बा",
        "early blight" to "अगेती झुलसा",
        "late blight" to "पछेती झुलसा",
        "leaf scorch" to "पत्ती झुलसा",
        "leaf mold" to "पत्ती फफूंद",
        "target spot" to "लक्ष्य धब्बा",
        "common rust" to "सामान्य रतुआ",
        "black rot" to "काला सड़न रोग",
        "apple scab" to "पपड़ी रोग",
        "mosaic virus" to "मोज़ेक विषाणु"
    ).firstOrNull { disease.contains(it.first) }?.second ?: disease

    return (cropsHi[crop] ?: cropEn) + " – " + diseaseHi
}

private fun recommendationFor(label: String, isHindi: Boolean): String {
    val value = label.lowercase()
    return when {
        value.contains("healthy") -> if (isHindi) "सामान्य सिंचाई और संतुलित पोषण जारी रखें। पत्तियों के दोनों तरफ नियमित निगरानी करें।" else "Continue suitable watering and balanced nutrition. Inspect both sides of leaves regularly."
        value.contains("spider mite") -> if (isHindi) "पत्तियों की निचली सतह और महीन जाले देखें। प्रभावित पौधे को अलग रखें और विशेषज्ञ से पुष्टि लें।" else "Inspect leaf undersides for fine webbing. Isolate affected plants and confirm the pest with a local expert."
        value.contains("virus") -> if (isHindi) "प्रभावित पौधों को छूने के बाद हाथ और औजार साफ करें। रोग फैलाने वाले कीटों की जाँच करें और विशेषज्ञ से सलाह लें।" else "Clean hands and tools after handling affected plants. Check for insect vectors and seek local expert advice."
        value.contains("bacterial") -> if (isHindi) "ऊपर से सिंचाई से बचें, पत्तियों को देर तक गीला न रखें और संक्रमित अवशेष हटाएँ।" else "Avoid overhead watering, keep foliage from staying wet and remove affected plant debris."
        listOf("blight", "mildew", "rust", "scab", "rot", "spot", "scorch").any { value.contains(it) } ->
            if (isHindi) "बहुत प्रभावित पत्तियाँ हटाकर नष्ट करें, पौधों के बीच हवा का आवागमन रखें और पत्तियों को अनावश्यक रूप से गीला न करें। रोग की पुष्टि स्थानीय कृषि विशेषज्ञ से कराएँ।"
            else "Remove badly affected leaves, improve airflow and avoid unnecessary leaf wetness. Confirm the disease with a local agricultural expert."
        else -> if (isHindi) "फोटो के साथ लक्षणों की जाँच करें। पानी, धूप और मिट्टी की स्थिति भी परखें; कोई दवा डालने से पहले स्थानीय विशेषज्ञ से सलाह लें।" else "Review symptoms alongside the photo. Check watering, light and soil conditions; consult a local expert before applying treatment."
    }
}

private fun categoryHindi(value: String): String = when (value) {
    "All" -> "सभी"; "Field crop" -> "खेत की फसल"; "Vegetable" -> "सब्जी"; "Fruit tree" -> "फलदार पेड़"
    "Indoor plant" -> "इनडोर पौधा"; "Ornamental flower" -> "सजावटी फूल"; "Medicinal / aromatic" -> "औषधीय / सुगंधित"; "Herb" -> "हर्ब"; else -> value
}
