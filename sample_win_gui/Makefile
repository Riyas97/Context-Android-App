CXX = g++
CXXFLAGS = -std=c++11 -Wall -static-libgcc -static-libstdc++ -mwindows
TARGET = sample_gui
ICON = icon

all:$(TARGET)

$(TARGET): $(TARGET).o $(ICON).o
	$(CXX) $(TARGET).o $(ICON).o $(CXXFLAGS) -o $(TARGET)

$(TARGET).o: $(TARGET).cpp
	$(CXX) -c $(TARGET).cpp

$(ICON).o: $(ICON).rc
	windres $(ICON).rc $(ICON).o	

clean:
	del $(ICON).o
	del $(TARGET).o
	del $(TARGET).exe