package feedbackLearning;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.Font;
import org.jfree.chart.plot.Plot;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: ART Testbed Team</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class HistoryChart
    extends JFreeChart {

  //permits a JFreeChart to store a master dataset, in addition to the displayed dataset

  XYSeriesCollection[] masterSeriesCollections; //for SingleCharts, ErrorBarCharts, MultipleCharts
  //DefaultErrorBarDataset[] masterErrorBarDatasets; //for ErrorBarCharts, MultipleErrorBarCharts

  public HistoryChart(String title, Font font,
                      Plot plot, boolean legend) {
    super(title, font, plot, legend);
    //initialize structures here
  }

  public void initializeSeriesCollectionHistory(int _numElts) {
    masterSeriesCollections = new XYSeriesCollection[_numElts];
    for (int i = 0; i < _numElts; i++) {
      masterSeriesCollections[i] = new XYSeriesCollection();
    }
  }

//  public void initializeErrorBarDatasetHistory(int _numElts) {
//    masterErrorBarDatasets = new DefaultErrorBarDataset[_numElts];
//    for (int i = 0; i < _numElts; i++) {
//      masterErrorBarDatasets[i] = new DefaultErrorBarDataset(null);
//    }
//  }

  public void putMasterSeriesCollection(XYSeriesCollection _coll) {
    masterSeriesCollections[0] = _coll;
  }

  public void putMasterSeriesCollections(XYSeriesCollection[] _coll) {
    masterSeriesCollections = _coll;
  }

//  public void putMasterErrorBarDataset(DefaultErrorBarDataset _dataset) {
//    masterErrorBarDatasets[0] = _dataset;
//  }
//
//  public void putMasterErrorBarDatasets(DefaultErrorBarDataset[] _dataset) {
//    masterErrorBarDatasets = _dataset;
//  }

  public XYSeriesCollection getMasterSeriesCollection() {
    return masterSeriesCollections[0];
  }

  public XYSeriesCollection[] getMasterSeriesCollections() {
    return masterSeriesCollections;
  }

//  public DefaultErrorBarDataset getMasterErrorBarDataset() {
//    return masterErrorBarDatasets[0];
//  }
//
//  public DefaultErrorBarDataset[] getMasterErrorBarDatasets() {
//    return masterErrorBarDatasets;
//  }
}
