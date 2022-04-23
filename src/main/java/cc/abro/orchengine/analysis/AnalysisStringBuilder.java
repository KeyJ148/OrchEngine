package cc.abro.orchengine.analysis;

public class AnalysisStringBuilder {

    public static final int STRING_COUNT = 2;

    private final Analyzer analyzer;

    public AnalysisStringBuilder(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public String getAnalysisString1() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ping: ");
        sb.append(analyzer.ping);
        sb.append(" (");
        sb.append(analyzer.pingMin);
        sb.append("-");
        sb.append(analyzer.pingMid);
        sb.append("-");
        sb.append(analyzer.pingMax);
        sb.append(")");
        sb.append("          ");

        sb.append("Speed TCP S/L, UDP S/L: ");
        sb.append(analyzer.sendTCP);
        sb.append("/");
        sb.append(analyzer.loadTCP);
        sb.append(", ");
        sb.append(analyzer.sendUDP);
        sb.append("/");
        sb.append(analyzer.loadUDP);
        sb.append(" kb/s");
        sb.append("          ");

        sb.append("Package TCP S/L, UDP S/L: ");
        sb.append(analyzer.sendPackageTCP);
        sb.append("/");
        sb.append(analyzer.loadPackageTCP);
        sb.append(", ");
        sb.append(analyzer.sendPackageUDP);
        sb.append("/");
        sb.append(analyzer.loadPackageUDP);
        sb.append("          ");

        return sb.toString();
    }

    public String getAnalysisString2() {
        StringBuilder sb = new StringBuilder();
        sb.append("FPS: ");
        sb.append(analyzer.loopsRender);
        sb.append("          ");

        sb.append("Duration update/render/sync: ");
        sb.append((analyzer.durationUpdate / analyzer.loopsUpdate / 1000));
        sb.append("/");
        sb.append((analyzer.durationRender / analyzer.loopsRender / 1000));
        sb.append("/");
        sb.append((analyzer.durationSync / analyzer.loopsSync / 1000));
        sb.append(" mks");
        sb.append("          ");

        sb.append("Objects (upd/rend/unsuit): ");
        sb.append(analyzer.objectsUpdatedSum);
        sb.append("/");
        sb.append(analyzer.objectsRenderedSum);
        sb.append("/");
        sb.append(analyzer.unsuitableObjectsRenderedSum);
        sb.append("          ");

        sb.append("Chunks (upd/rend): ");
        sb.append(analyzer.chunksUpdatedSum);
        sb.append("/");
        sb.append(analyzer.chunksRenderedSum);
        sb.append("          ");
        sb.append("Layers (upd/rend): ");
        sb.append(analyzer.layersCountUpdated);
        sb.append("/");
        sb.append(analyzer.layersCountRenderer);
        sb.append("          ");

        sb.append("Memory: ");
        sb.append((analyzer.totalMem - analyzer.freeMem) / 1024 / 1024 );
        sb.append("/");
        sb.append(analyzer.totalMem / 1024 / 1024 );
        sb.append("/");
        sb.append(analyzer.maxMem / 1024 / 1024 );
        sb.append(" MB          ");

        return sb.toString();
    }
}
