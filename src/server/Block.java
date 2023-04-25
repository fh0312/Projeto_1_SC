package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Block {
	
	private byte[] hash;
	
	private long blk_id;
	
	private long n_trx;
	
	private List<Transacao> trx;
	
	private byte[] sigServer;
	
	private File file;
	
	private String dir = "server_files//blks//";
	
	
	public Block(byte[] hash,long id) {
		this.hash = new byte[32];
		blk_id = id;
		new File(dir.substring(0,dir.length()-2)).mkdir();
		file = new File (dir+"block_"+blk_id+".blk");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(hash==null) {
			Arrays.fill(this.hash, (byte) 0);
		}
		else {
			this.hash = hash;
		}
		n_trx = 0;
		trx = new ArrayList<>();
	}
	
	public void addTr(Transacao t) {
		trx.add(t);
		this.n_trx++;
		writeAll();
	}
	
	private void writeAll() {
		PrintWriter pw =null;
		StringBuilder sb = new StringBuilder();
		try {
			pw= new PrintWriter(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		sb.append(Arrays.toString(this.hash)+"\n");
		sb.append("blk_id = "+this.blk_id+"\n");
		sb.append("n_trx = "+this.n_trx+"\n");
		for(Transacao t : trx) {
			sb.append(t.toString()+"\n");
		}
		sb.append(this.sigServer);
		pw.write(sb.toString());
		pw.close();
		
	}
	
	public byte[] close() {
		String path = this.file.getParent()+"//arquivo";
		File dir = new File(path);
		dir.mkdir();
		Path sourcePath = Paths.get(this.file.getAbsolutePath());
        Path destinationPath = Paths.get(dir.getAbsolutePath()+"//"+this.file.getPath());

        try {
            Files.move(sourcePath, destinationPath);
        }
        catch(Exception e) {
        	
        }
        try {
        	FileInputStream inputStream = new FileInputStream(this.file);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            DigestInputStream digestInputStream = new DigestInputStream(inputStream, digest);
            byte[] buff = new byte[4096];
            while (digestInputStream.read(buff) > -1);
            this.hash = digest.digest();
        }
        catch(Exception e) {
        	
        }
        
        return hash;
	}

	public byte[] getHash() {
		return hash;
	}

	public void setHash(byte[] hash) {
		this.hash = hash;
	}

	public long getBlk_id() {
		return blk_id;
	}

	public void setBlk_id(long blk_id) {
		this.blk_id = blk_id;
	}

	public long getN_trx() {
		return n_trx;
	}

	public void setN_trx(long n_trx) {
		this.n_trx = n_trx;
	}

	public List<Transacao> getTrx() {
		return trx;
	}

	public void setTrx(List<Transacao> trx) {
		this.trx = trx;
	}

	public byte[] getSigServer() {
		return sigServer;
	}

	public void setSigServer(byte[] sigServer) {
		this.sigServer = sigServer;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
