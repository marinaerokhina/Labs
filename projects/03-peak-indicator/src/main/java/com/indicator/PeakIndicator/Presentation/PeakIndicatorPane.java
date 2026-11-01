package com.indicator.PeakIndicator.Presentation;

import com.indicator.PeakIndicator.Application.*;
import com.indicator.PeakIndicator.Domain.*;

import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.canvas.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PeakIndicatorPane extends Pane {
    private final Canvas canvas = new Canvas();
    private final IndicatorService service;
    private IndicatorSnapshot state;
    
    private final double originalThMin;
    private final double originalThMax;
    
    private final Text textHelper = new Text();

    public PeakIndicatorPane(
            double dataMin,
            double dataMax,
            double thMin,
            double thMax,
            double peakDecayPerSecond,
            int historyCapacity,
            DataSource dataSource
    ) {
        this.originalThMin = thMin;
        this.originalThMax = thMax;
        
        textHelper.setFont(Font.font(12));
        
        getChildren().add(canvas);
        widthProperty().addListener((obs, o, n) -> layoutCanvas());
        heightProperty().addListener((obs, o, n) -> layoutCanvas());
        setPadding(new Insets(10));
        
        service = IndicatorService.createWithRealScale(
                dataMin, dataMax,
                thMin, thMax,
                peakDecayPerSecond,
                historyCapacity,
                dataSource
        );
        
        service.setLevelListener(e -> Platform.runLater(() -> {
            this.state = service.snapshot();
            redraw();
        }));
    }

    private void layoutCanvas() {
        double w = Math.max(1, getWidth() - getPadding().getLeft() - getPadding().getRight());
        double h = Math.max(1, getHeight() - getPadding().getTop() - getPadding().getBottom());
        canvas.setLayoutX(getPadding().getLeft());
        canvas.setLayoutY(getPadding().getTop());
        canvas.setWidth(w);
        canvas.setHeight(h);
        redraw();
    }

    public void setSnapshot(IndicatorSnapshot state) {
        this.state = state;
        redraw();
    }
    
    public void setThresholdListener(ThresholdListener listener) {
        service.setThresholdListener(listener);
    }
    
    public void onTick() {
        service.onTick();
    }

    private void redraw() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        g.clearRect(0, 0, w, h);
        if (state == null) return;
                
        g.setFill(Color.web("#f2f2f7"));
        g.fillRect(0, 0, w, h);
        g.setStroke(Color.web("#cccccc"));
        g.strokeRect(0, 0, w, h);
                
        double minY = h * (1.0 - state.thresholds.min().asDouble());
        double maxY = h * (1.0 - state.thresholds.max().asDouble());
                
        g.setStroke(Color.GRAY);
        g.setLineDashes(6);
        g.strokeLine(0, minY, w, minY);
        g.strokeLine(0, maxY, w, maxY);
        g.setLineDashes(null);
        
        Font font = Font.font(12);
        g.setFont(font);
        g.setFill(Color.GRAY);
        
        String minText = String.format("%.1f", originalThMin);
        textHelper.setText(minText);
        double minTextWidth = textHelper.getLayoutBounds().getWidth();
        double minTextX = w - minTextWidth - 5;
        double minTextY = minY - 5;
        g.fillText(minText, minTextX, minTextY);
        
        String maxText = String.format("%.1f", originalThMax);
        textHelper.setText(maxText);
        double maxTextWidth = textHelper.getLayoutBounds().getWidth();
        double maxTextX = w - maxTextWidth - 5;
        double maxTextY = maxY - 5;
        g.fillText(maxText, maxTextX, maxTextY);
                
        double curH = h * state.current.asDouble();
        g.setFill(Color.DODGERBLUE);
        g.fillRect(w * 0.25, h - curH, w * 0.5, curH);
                
        double peakY = h * (1.0 - state.peak.asDouble());
        g.setStroke(Color.CRIMSON);
        g.setLineWidth(3);
        g.strokeLine(w * 0.15, peakY, w * 0.85, peakY);
    }
}

