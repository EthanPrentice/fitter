package com.portalpirates.cufit.datamodel.data.measure

import android.icu.util.Measure
import android.icu.util.MeasureUnit
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*

abstract class FitMeasure(number: Number, unit: MeasureUnit, val dateLogged: Date) : Measure(number, unit) {

    fun convertFieldsToHashMap(prefix: String): HashMap<String, Any?> {
        val hashMap = HashMap<String, Any?>()
        addFieldsToHashMap(hashMap, prefix)
        return hashMap
    }

    fun addFieldsToHashMap(hashMap: HashMap<String, Any?>, prefix: String) {
        hashMap.apply {
            put("$prefix.$CLOUD_NUMBER", number)
            put("$prefix.$CLOUD_UNIT", CloudUnit.getFromMeasureUnit(unit)!!.cloudId)
            put("$prefix.$CLOUD_DATE_LOGGED", dateLogged)
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
    }

}