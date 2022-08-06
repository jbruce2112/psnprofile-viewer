package com.bruce32.psnprofileviewer

import com.bruce32.psnprofileviewer.common.ResourceStringSource
import org.apache.commons.text.StringEscapeUtils
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.FileInputStream
import java.util.regex.Matcher
import javax.xml.parsers.DocumentBuilderFactory

object FakeResourceStringSource : ResourceStringSource {

    private val resIdToString =
        readStringsFromXml(filename = "../app/src/main/res/values/strings.xml")

    override fun getString(resId: Int, vararg args: Any): String {
        val string = resIdToString[resId] ?: throw RuntimeException("Can't find resourceId $resId")
        return formattedString(string, *args)
    }

    private fun readStringsFromXml(filename: String): HashMap<Int, String> {
        FileInputStream(filename).use {
            val dbFactory = DocumentBuilderFactory.newInstance()
            val dBuilder = dbFactory.newDocumentBuilder()
            val doc = dBuilder.parse(it)

            val root = doc.documentElement
            root.normalize()
            return parseStrings(root)
        }
    }

    private fun parseStrings(root: Element): HashMap<Int, String> {
        val elements = root.getElementsByTagName("string")

        val map = HashMap<Int, String>()
        for (i in 0 until elements.length) {
            val item = elements.item(i)
            val name = item.attributes.getNamedItem("name").nodeValue
            val resId = getResId(name)
            map[resId] = item.unescapedValue()
        }
        return map
    }

    private fun Node.unescapedValue(): String {
        val value = firstChild.nodeValue
        return StringEscapeUtils.unescapeJava(value)
    }

    private fun getResId(resName: String): Int {
        val idField = R.string::class.java.getDeclaredField(resName)
        return idField.getInt(idField)
    }

    private fun formattedString(string: String, vararg formatArgs: Any): String {
        var formatted = string
        formatArgs.forEachIndexed { index, arg ->
            val argAsString = Matcher.quoteReplacement(arg.toString())
            if (index == 0) {
                formatted = formatted.replace(Regex("%[a-z]"), argAsString)
            }
            formatted = formatted.replace(Regex("%${index + 1}\\$."), argAsString)
        }
        return formatted
    }
}