package com.info.web.pojo.cspojo;

import lombok.Data;

import java.util.List;

@Data
public class TotalJson {
	private List<SeriesObj> seriesObj;
	private List<String> categories;

}
