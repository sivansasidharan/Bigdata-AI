/**
 * @author sivan.sasidharan
 *
 */

package com.ey.analytics.adapt.adapters.mongodb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.util.JSON;

public class MongoOperations {
	
	private static Logger logger = Logger.getLogger(MongoOperations.class);
	
	final static ObjectMapper mapper = new ObjectMapper();
	
	private DB database = null;
	
	private Mongo mongo = null;
	
	/**
	 * Construct a new MongoOperations and sets the values for 
	 * <code>Mongo</code>
	 * <code>DB</code><p>
	 * <br>
	 * @param hostName <code>String</code>
	 * @param port <code>String</code>
	 * @param DBName <code>String</code>
	 * <br>
	 * </p>
	 */
	public MongoOperations( String hostName, String port, String DBName) {
		try {
			mongo = new Mongo(hostName, Integer.parseInt(port));
			database = mongo.getDB(DBName);
		}
		catch (Exception e) {
			logger.error("Error Message-->Exception occured in constructor" + e.getMessage());
		}
	}
	
	/**
	 * Save the collection.<p>
	 * <br>
	 * @param collectionName
	 * @param documentJson
	 * @return <code>Boolean</code>
	 * <code>{@link com.ey.analytics.adapt.adapters.mongodb}</code>
	 * </br>
	 * </p>
	 */
	public boolean save( String collectionName, String documentJson) {
		boolean success = false;
		
		try {
			database.requestStart();
			DBCollection collection = null;
			collection = database.getCollection(collectionName);
			BasicDBObject documentObject = (BasicDBObject) JSON.parse(documentJson);
			collection.save(documentObject);
			// Get the collection
			if (collection != null && collection.getCount() > 0) {
				success = true;
			}
		}
		catch (Exception e) {
			logger.error("Error Message-->Exception occured in save method" + e.getMessage());
		}
		finally {
			database.requestDone();
		}
		return success;
	}
	
	/**
	 * Save the collection.<p>
	 * <br>
	 * @param collectionName
	 * @param documentJson
	 * @return <code>Boolean</code>
	 * <code>{@link com.ey.analytics.adapt.adapters.mongodb}</code>
	 * </br>
	 * </p>
	 */
	public boolean save( String collectionName, Map<String,Object> documentMap) {
		boolean success = false;
		try {
			database.requestStart();
			DBCollection collection = null;
			if (documentMap != null && documentMap.size() > 0) {
				collection = database.getCollection(collectionName);
				collection.save(new BasicDBObject(documentMap));
				// Get the collection
				if (collection != null && collection.getCount() > 0) {
					success = true;
				}
			}
		}
		catch (Exception e) {
			logger.error("Error Message-->Exception occured in save method" + e.getMessage());
		}
		finally {
			database.requestDone();
		}
		return success;
	}
	
	/**
	 * 
	 * Update collection.<p>
	 * <br>
	 * @param collectionName
	 * @param criteriaMap
	 * @param documentMap
	 * @return <code></code>
	 * <code>{@link com.ey.analytics.adapt.adapters.mongodb}</code>
	 * Desc of the param.
	 * <br>
	 * @throws <code></code>
	 * <code>{@link exception class }</code>
	 * Throws a <code>exception class<code> if any error occurs.
	 * <br>
	 * </p>
	 */
	public boolean update( String collectionName, Map<String,Object> criteriaMap,
						   Map<String,Object> documentMap) {
		boolean success = false;
		try {
			database.requestStart();
			DBCollection collection = null;
			collection = database.getCollection(collectionName);
			if (documentMap != null && criteriaMap != null && criteriaMap.size() > 0
						&& criteriaMap.size() > 0) {
				BasicDBObject documentObject = new BasicDBObject(documentMap);
				BasicDBObject criteriaObject = new BasicDBObject(criteriaMap);
				if (collection.findOne(criteriaObject) != null) {
					collection.update(criteriaObject, documentObject);
					if (collection != null && collection.getCount() > 0) {
						success = true;
					}
				}
				else {
					logger.error("No maching record found");
				}
			}
		}
		catch (Exception e) {
			logger.error("Error Message-->Exception occured in save method" + e.getMessage());
		}
		finally {
			database.requestDone();
		}
		return success;
	}
	
	/**
	 * Save Or Update <p>
	 * <br>
	 * @param collectionName
	 * @param criteriaMap
	 * @param documentMap
	 * @return <code></code>
	 * <code>{@link com.ey.analytics.adapt.adapters.mongodb}</code>
	 * Desc of the param.
	 * <br>
	 * @throws <code></code>
	 * <code>{@link exception class }</code>
	 * Throws a <code>exception class<code> if any error occurs.
	 * <br>
	 * </p>
	 */
	public boolean saveOrUpdate( String collectionName, Map<String,Object> criteriaMap,
								 Map<String,Object> documentMap) {
		boolean success = false;
		
		try {
			database.requestStart();
			DBCollection collection = null;
			collection = database.getCollection(collectionName);
			if (documentMap != null && criteriaMap != null && criteriaMap.size() > 0
						&& criteriaMap.size() > 0) {
				BasicDBObject documentObject = new BasicDBObject(documentMap);
				BasicDBObject criteriaObject = new BasicDBObject(criteriaMap);
				if (collection.findOne(criteriaObject) != null) {
					collection.update(criteriaObject, documentObject);
				}
				else {
					collection.save(documentObject);
				}
				// Get the collection
				if (collection != null && collection.getCount() > 0) {
					success = true;
				}
			}
		}
		catch (Exception e) {
			logger.error("Error Message-->Exception occured in save method" + e.getMessage());
		}
		finally {
			database.requestDone();
		}
		return success;
	}
	
	/**
	 * Save or update..<p>
	 * <br>
	 * @param collectionName
	 * @param criteriaJson
	 * @param documentJson
	 * @return <code></code>
	 * <code>{@link com.ey.analytics.adapt.adapters.mongodb}</code>
	 * Desc of the param.
	 * <br>
	 * @throws <code></code>
	 * <code>{@link exception class }</code>
	 * Throws a <code>exception class<code> if any error occurs.
	 * <br>
	 * </p>
	 */
	public boolean saveOrUpdate( String collectionName, String criteriaJson, String documentJson) {
		boolean success = false;
		
		try {
			database.requestStart();
			DBCollection collection = null;
			collection = database.getCollection(collectionName);
			BasicDBObject documentObject = (BasicDBObject) JSON.parse(documentJson);
			BasicDBObject criteriaObject = (BasicDBObject) JSON.parse(criteriaJson);
			
			if (documentObject != null) {
				if (collection.findOne(criteriaObject) != null) {
					collection.update(criteriaObject, documentObject);
				}
				else {
					collection.save(documentObject);
				}
				// Get the collection
				if (collection != null && collection.getCount() > 0) {
					success = true;
				}
			}
		}
		catch (Exception e) {
			logger.error("Error Message-->Exception occured in save method" + e.getMessage());
		}
		finally {
			database.requestDone();
		}
		return success;
	}
	
	/**
	 * Find All Collections<p>
	 * <br>
	 * @param collectionName
	 * @return <code></code>
	 * <code>{@link com.ey.analytics.adapt.adapters.mongodb}</code>
	 * Desc of the param.
	 * <br>
	 * @throws <code></code>
	 * <code>{@link exception class }</code>
	 * Throws a <code>exception class<code> if any error occurs.
	 * <br>
	 * </p>
	 */
	public List<Map> findAll( String collectionName) {
		List<Map> collectionData = null;
		DBCursor cursor = null;
		DBObject obj = null;
		try {
			database.requestStart();
			DBCollection collection = database.getCollection(collectionName);
			if (collection != null) {
				collectionData = new ArrayList<Map>();
				cursor = collection.find();
				while (cursor != null && cursor.hasNext()) {
					obj = cursor.next();
					collectionData.add(obj.toMap());
				}
			}
		}
		catch (Exception e) {
			logger.error("Error Message-->Exception occured in findAll method" + e.getMessage());
		}
		finally {
			database.requestDone();
		}
		return collectionData;
	}
	
	/**
	 * Find All Collections <p>
	 * <br>
	 * @param collectionName
	 * @param criteriaMap
	 * @return <code></code>
	 * <code>{@link com.ey.analytics.adapt.adapters.mongodb}</code>
	 * Desc of the param.
	 * <br>
	 * @throws <code></code>
	 * <code>{@link exception class }</code>
	 * Throws a <code>exception class<code> if any error occurs.
	 * <br>
	 * </p>
	 */
	public List<Map> findAll( String collectionName, Map<String,Object> criteriaMap) {
		List<Map> collectionData = null;
		DBCursor cursor = null;
		DBObject obj = null;
		try {
			database.requestStart();
			DBCollection collection = database.getCollection(collectionName);
			if (collection != null && criteriaMap != null && criteriaMap.size() > 0) {
				collectionData = new ArrayList<Map>();
				cursor = collection.find(new BasicDBObject(criteriaMap));
				while (cursor != null && cursor.hasNext()) {
					obj = cursor.next();
					collectionData.add(obj.toMap());
				}
			}
		}
		catch (Exception e) {
			logger.error("Error Message-->Exception occured in findAll method" + e.getMessage());
		}
		finally {
			database.requestDone();
		}
		return collectionData;
	}
	
	/**
	 * Find One Impl<p>
	 * <br>
	 * @param collectionName
	 * @return <code></code>
	 * <code>{@link com.ey.analytics.adapt.adapters.mongodb}</code>
	 * Desc of the param.
	 * <br>
	 * @throws <code></code>
	 * <code>{@link exception class }</code>
	 * Throws a <code>exception class<code> if any error occurs.
	 * <br>
	 * </p>
	 */
	public Map findOne( String collectionName) {
		
		try {
			database.requestStart();
			DBCollection collection = database.getCollection(collectionName);
			if (collection != null) {
				DBObject resultObject = collection.findOne();
				if (resultObject != null && resultObject.toMap() != null && resultObject.toMap().size() > 0) { return resultObject
					.toMap(); }
			}
		}
		catch (Exception e) {
			logger.error("Error Message-->Exception occured in findAll method" + e.getMessage());
		}
		finally {
			database.requestDone();
		}
		return null;
	}
	
	/**
	 * Find One 
	 * desc.<p>
	 * <br>
	 * @param collectionName
	 * @param criteriaMap
	 * @return <code></code>
	 * <code>{@link com.ey.analytics.adapt.adapters.mongodb}</code>
	 * Desc of the param.
	 * <br>
	 * @throws <code></code>
	 * <code>{@link exception class }</code>
	 * Throws a <code>exception class<code> if any error occurs.
	 * <br>
	 * </p>
	 */
	public Map findOne( String collectionName, Map<String,Object> criteriaMap) {
		
		try {
			database.requestStart();
			DBCollection collection = database.getCollection(collectionName);
			if (collection != null && criteriaMap != null && criteriaMap.size() > 0) {
				DBObject resultObject = collection.findOne(new BasicDBObject(criteriaMap));
				if (resultObject != null && resultObject.toMap() != null && resultObject.toMap().size() > 0) { return resultObject
					.toMap(); }
			}
		}
		catch (Exception e) {
			logger.error("Error Message-->Exception occured in findOne method" + e.getMessage());
		}
		finally {
			database.requestDone();
		}
		return null;
	}
	
	/**
	 * Look up<p>
	 * <br>
	 * @param collectionName
	 * @param fieldName
	 * @return <code></code>
	 * <code>{@link com.ey.analytics.adapt.adapters.mongodb}</code>
	 * Desc of the param.
	 * <br>
	 * @throws <code></code>
	 * <code>{@link exception class }</code>
	 * Throws a <code>exception class<code> if any error occurs.
	 * <br>
	 * </p>
	 */
	public Object lookup( String collectionName, String fieldName) {
		try {
			database.requestStart();
			DBCollection collection = database.getCollection(collectionName);
			if (collection != null) {
				DBObject resultObject = collection.findOne();
				if (resultObject != null && resultObject.toMap() != null && resultObject.toMap().size() > 0) { return resultObject
					.toMap().get(fieldName); }
			}
		}
		catch (Exception e) {
			logger.error("Error Message-->Exception occured in lookup method" + e.getMessage());
		}
		finally {
			database.requestDone();
		}
		return null;
	}
	
	/**
	 * Look Up <p>
	 * <br>
	 * @param collectionName
	 * @param criteriaMap
	 * @param fieldName
	 * @return <code></code>
	 * <code>{@link com.ey.analytics.adapt.adapters.mongodb}</code>
	 * Desc of the param.
	 * <br>
	 * @throws <code></code>
	 * <code>{@link exception class }</code>
	 * Throws a <code>exception class<code> if any error occurs.
	 * <br>
	 * </p>
	 */
	public Object lookup( String collectionName, Map<String,Object> criteriaMap, String fieldName) {
		try {
			database.requestStart();
			DBCollection collection = database.getCollection(collectionName);
			if (collection != null && criteriaMap != null && criteriaMap.size() > 0) {
				DBObject resultObject = collection.findOne(new BasicDBObject(criteriaMap));
				if (resultObject != null && resultObject.toMap() != null && resultObject.toMap().size() > 0) { return resultObject
					.toMap().get(fieldName); }
			}
		}
		catch (Exception e) {
			logger.error("Error Message-->Exception occured in lookup method" + e.getMessage());
		}
		finally {
			database.requestDone();
		}
		return null;
	}
	
	public void saveImageIntoMongoDB( String imagePath, String dbFileName) throws IOException {
		
		com.mongodb.gridfs.GridFS gfsPhoto = null;
		try {
			database.requestStart();
			File imageFile = new File(imagePath);
			gfsPhoto = new com.mongodb.gridfs.GridFS(database, "companyLogos");
			com.mongodb.gridfs.GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);
			gfsFile.setFilename(dbFileName);
			gfsFile.save();
		}
		catch (Exception e) {
			logger.error("Error Message-->Exception occured in lookup method" + e.getMessage());
		}
		finally {
		   // GridFSDBFile imageForOutput = gfsPhoto.findOne(dbFileName);
		   // imageForOutput.writeTo("resources/"+dbFileName+"DemoImageNew.gif");
			database.requestDone();
		}
	}
}
