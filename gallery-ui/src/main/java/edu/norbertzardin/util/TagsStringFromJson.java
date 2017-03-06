package edu.norbertzardin.util;
import org.zkoss.zk.ui.util.Clients;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TagsStringFromJson {
    public static List<String> convert () throws NoSuchMethodException, ScriptException, FileNotFoundException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByMimeType("text/javascript");

        engine.eval(new FileReader("META-INF/resources/webjars/jquery/3.1.1/jquery.js"));
        engine.eval("var $ = jQuery; var getTags = function () { $('#imageTags').tokenfield('getTokens'); } ");
        Invocable invocable = (Invocable) engine;


        Object result = invocable.invokeFunction("getTags");
        System.out.println(result);
        System.out.println(result.getClass());
        System.out.println(result.toString());


        return new ArrayList<String>();
    }

}
