package com.ethanprentice.fitter.datamodel.data.measure

import android.icu.util.Measure
import android.icu.util.MeasureUnit
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*
import kotlin.collections.HashMap

abstract class FitMeasure(number: Number, unit: MeasureUnit, val dateLogged: Date) : Measure(number, unit) {

    fun convertFieldsToHashMap(fieldName: String): HashMap<String, Any?> {
        val hashMap = HashMap<String, Any?>()
        addFieldsToHashMap(hashMap, "$fieldName")
        return hashMap
    }

    fun convertFieldsToHashMap(): HashMap<String, Any?> {
        val hashMap = HashMap<String, Any?>()
        addFieldsToHashMap(hashMap, "")
        return hashMap
    }

    fun addFieldsToHashMap(hashMap: HashMap<String, Any?>, prefix: String) {
        val realPrefix = if (prefix.isNotBlank()) "$prefix." else ""
        hashMap.apply {
            put("$realPrefix$CLOUD_NUMBER", number)
            put("$realPrefix$CLOUD_UNIT", CloudUnit.getFromMeasureUnit(unit)!!.cloudId)
            put("$realPrefix$CLOUD_DATE_LOGGED", dateLogged)
        }
    }

    companion object {
        private const val CLOUD_NUMBER = "value"
        private const val CLOUD_UNIT = "unit"
        private const val CLOUD_DATE_LOGGED = "date_logged"

        /**
         * @return a FitMeasure from the document if possible, null otherwise
         */
        fun <T : FitMeasure> getFromDocument(doc: DocumentSnapshot, fieldName: String, factory: (Number, MeasureUnit, Date) -> T): T? {
            val number = doc.getDouble("$fieldName.$CLOUD_NUMBER") ?: return null
            val unitStr = doc.getString("$fieldName.$CLOUD_UNIT") ?: return null
            val dateLogged = doc.getDate("$fieldName.$CLOUD_DATE_LOGGED") ?: return null

            val unit = CloudUnit.getFromCloudId(unitStr) ?: return null

            return factory(number, unit.measureUnit, dateLogged)
        }

        fun <T : FitMeasure> getFromHashMap(hashMap: HashMap<String, Any?>, fieldName: String, factory: (Number, MeasureUnit, Date) -> T): T? {
            val number = (hashMap["$fieldName.$CLOUD_NUMBER"] as? Number)?.toDouble() ?: return null
            val unitStr = hashMap["$fieldName.$CLOUD_UNIT"] as? String ?: return null
            val dateLogged = (hashMap["$fieldName.$CLOUD_DATE_LOGGED"] as? Timestamp)?.toDate() ?: return null

            val unit = CloudUnit.getFromCloudId(unitStr) ?: return null

            return factory(number, unit.measureUnit, dateLogged)
        }

        fun <T : FitMeasure> getFromHashMap(hashMap: HashMap<String, Any?>, factory: (Number, MeasureUnit, Date) -> T): T? {
            val number = (hashMap[CLOUD_NUMBER] as? Number)?.toFloat() ?: return null
            val unitStr = hashMap[CLOUD_UNIT] as? String ?: return null
            val dateLogged = (hashMap[CLOUD_DATE_LOGGED] as? Timestamp)?.toDate() ?: return null

            val unit = CloudUnit.getFromCloudId(unitStr) ?: return null

            return factory(number, unit.measureUnit, dateLogged)
        }
    }

}