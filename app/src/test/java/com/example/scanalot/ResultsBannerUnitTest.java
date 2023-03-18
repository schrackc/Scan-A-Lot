package com.example.scanalot;

import android.os.Bundle;
import android.util.Xml;

import androidx.constraintlayout.widget.ConstraintSet;

import com.example.scanalot.databinding.ActivityMainBinding;
import com.google.rpc.context.AttributeContext;

import org.junit.Assert;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ResultsBannerUnitTest {


    @Test
    public void resultsBannerCorrectLot() {
        String strExpectedText = "Vehicle in Correct Lot";
        String strExpectedColor = "Vehicle in Correct Lot";
        //Test run
        ResultsFragment test = new ResultsFragment();
        test.strLicenseNumber = "ABC 1234";
        test.strLicenseState = "Pennsylvania";
        String value = test.getString(R.id.ResultTextView);
        Assert.assertEquals(strExpectedText, test.strBannerText);
        Assert.assertEquals(strExpectedColor, test.iBannerColor);
    }
}
