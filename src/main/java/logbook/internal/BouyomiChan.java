package logbook.internal;

import ch.qos.logback.core.encoder.EchoEncoder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 棒読みちゃん
 *
 */
public class BouyomiChan {

    private final InetSocketAddress addr;

    /** 音量(0～100, -1) */
    private short volume = -1;
    /** 速度(50～300, -1) */
    private short speed = -1;
    /** 音程(50～200, -1) */
    private short tone = -1;
    /** 声質(1～8, 0) */
    private short voice = 0;

    private BouyomiChan(String host, int port) {
        this.addr = new InetSocketAddress(host, port);
    }

    /**
     * 音量を設定します。
     * 
     * @param volume 音量(0～100, -1)
     * @return このオブジェクトへの参照。
     */
    public BouyomiChan setVolume(int volume) {
        this.volume = (short) volume;
        return this;
    }

    /**
     * 速度を設定します。
     * 
     * @param speed 速度(50～300, -1)
     * @return このオブジェクトへの参照。
     */
    public BouyomiChan setSpeed(int speed) {
        this.speed = (short) speed;
        return this;
    }

    /**
     * 音程を設定します。
     * 
     * @param tone 音程(50～200, -1)
     * @return このオブジェクトへの参照。
     */
    public BouyomiChan setTone(int tone) {
        this.tone = (short) tone;
        return this;
    }

    /**
     * 声質を設定します。
     * 
     * @param voice 声質(1～8, 0)
     * @return このオブジェクトへの参照。
     */
    public BouyomiChan setVoice(int voice) {
        this.voice = (short) voice;
        return this;
    }

    /**
     * 読み上げを一時停止します。
     * @throws IOException 接続に失敗した、または要求を送信出来ない場合。
     */
    public void pasuse() throws IOException {
        this.command((short) 16);
    }

    /**
     * 読み上げを再開します。
     * @throws IOException 接続に失敗した、または要求を送信出来ない場合。
     */
    public void resume() throws IOException {
        this.command((short) 32);
    }

    /**
     * 次の文章を読み上げます。
     * @throws IOException 接続に失敗した、または要求を送信出来ない場合。
     */
    public void skip() throws IOException {
        this.command((short) 48);
    }

    /**
     * 残りの文章をキャンセルします。
     * @throws IOException 接続に失敗した、または要求を送信出来ない場合。
     */
    public void clear() throws IOException {
        this.command((short) 64);
    }

    /**
     * 文章を読み上げます。
     * 
     * @param text 文章
     * @throws IOException 接続に失敗した、または要求を送信出来ない場合。
     */
    public void speak(String text) throws IOException {
        LoggerHolder.get().info("**************" + text);
        try {
            byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
            int length = textBytes.length;

            LoggerHolder.get().info("************** 1" + Arrays.toString(textBytes));
            ByteBuffer buffer = ByteBuffer.allocate(15 + length);
            LoggerHolder.get().info("************** 2" + buffer);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            LoggerHolder.get().info("************** 3" + buffer);
            // コマンド
            buffer.putShort((short) 1);
            LoggerHolder.get().info("************** 4" + buffer);
            // 速度
            LoggerHolder.get().info("************** 5" + buffer);
            buffer.putShort(this.speed);
            // 音程
            LoggerHolder.get().info("************** 6" + buffer);
            buffer.putShort(this.tone);
            // 音量
            LoggerHolder.get().info("************** 7" + buffer);
            buffer.putShort(this.volume);
            // 声質
            LoggerHolder.get().info("************** 8" + buffer);
            buffer.putShort(this.voice);
            // エンコード(UTF-8)
            LoggerHolder.get().info("************** 9" + buffer);
            buffer.put((byte) 0);
            // 長さ
            LoggerHolder.get().info("************** 10" + buffer);
            buffer.putInt(length);
            // 文章
            LoggerHolder.get().info("************** 11" + buffer);
            buffer.put(textBytes);

            LoggerHolder.get().info("************** 12" + buffer);
//            buffer.position(0);
            LoggerHolder.get().info("************** 13" + buffer);
            this.send(buffer);
        }catch (Throwable e){
            LoggerHolder.get().info("************** e" + e);
            throw e;
        }
    }

    private void command(short command) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(command);

        buffer.position(0);
        this.send(buffer);
    }

    private void send(ByteBuffer data) throws IOException {
        LoggerHolder.get().info(data);
        Socket socket = new Socket();
        socket.connect(this.addr, (int) TimeUnit.SECONDS.toMillis(1));
        try (OutputStream out = socket.getOutputStream()) {
            LoggerHolder.get().info("///////" + data);
            try (WritableByteChannel channel = Channels.newChannel(out)) {
                LoggerHolder.get().info("///////??????data:" + data);
                LoggerHolder.get().info("///////??????:channel:" + channel);
                data.rewind();
                LoggerHolder.get().info("///////rewind probrem");
                channel.write(data);
            }
        }catch(Throwable e){
            LoggerHolder.get().error("socket error." ,e);
            throw e;
        } finally {
            socket.close();
        }
    }

    /**
     * 棒読みちゃんへデフォルトの設定を使って接続します。
     * 
     * @return 棒読みちゃん
     */
    public static BouyomiChan create() {
        return new BouyomiChan("localhost", 50001);
    }

    /**
     * 棒読みちゃんへ接続します。
     * 
     * @param host リモートホスト
     * @param port リモートポート
     * @return 棒読みちゃん
     */
    public static BouyomiChan create(String host, int port) {
        return new BouyomiChan(host, port);
    }

}
