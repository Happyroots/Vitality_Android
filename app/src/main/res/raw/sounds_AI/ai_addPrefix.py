import os

prefix = 'ai_'
# Specify the folder path
folder_path = '/home/dry/Documents/Android/tests/app/src/main/res/raw/sounds_AI'

# Iterate through all files in the specified folder
for filename in os.listdir(folder_path):
    # Construct the full file path
    old_file_path = os.path.join(folder_path, filename)
    
    # Check if it's a file (not a directory)
    if os.path.isfile(old_file_path):
        # Create the new file name with the 'ai_' prefix
        new_filename = prefix + filename
        new_file_path = os.path.join(folder_path, new_filename)
        
        # Rename the file
        os.rename(old_file_path, new_file_path)

print("Files renamed successfully!")
